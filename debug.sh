docker run --rm -t -p 9090:8080 -p 5001:5000 -v $(pwd)/application.properties:/secrets/application.properties \
                                -v $(pwd)/javamelody:/javamelody \
                                -v $(pwd)/../laneweb-content:/laneweb-content laneweb:latest \
            /usr/bin/tini \
            -- \
            java \
            -agentlib:jdwp=transport=dt_socket,address=*:5000,server=y,suspend=n \
            -Xmx450m \
            -Dedu.stanford.irt.laneweb.live-base=file:/laneweb-content \
            -Dspring.profiles.active=default \
            -Dspring.session.store-type=none \
            -Dspring.main.allow-bean-definition-overriding=true \
            -Dedu.stanford.irt.laneweb.log.level=INFO \
            -Dspring.config.location=file:/secrets/application.properties \
            -Djava.security.krb5.conf=/config/krb5.conf \
            -Djava.security.auth.login.config=/config/jaas.conf \
            -Dsun.net.client.defaultConnectTimeout=5000 \
            -Dsun.net.client.defaultReadTimeout=15000 \
            -Djavamelody.storage-directory=/javamelody \
            -Djava.net.preferIPv4Stack=true \
            -jar \
            laneweb.war \
