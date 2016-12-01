package edu.stanford.irt.laneweb.user;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import edu.stanford.irt.laneweb.LanewebException;

public class User implements Serializable {

    private static final String AT = "@";

    private static final String AT_STANFORD_EDU = "@stanford.edu";

    private static final long serialVersionUID = 1L;

    private String email;

    private String hashedId;

    private String hashKey;

    private String id;

    private boolean isStanfordUser;

    private String name;

    public User(final String id, final String name, final String email, final String hashKey) {
        if (id.indexOf('@') == -1) {
            throw new LanewebException("domain missing from id: " + id);
        }
        this.id = id;
        this.isStanfordUser = id.indexOf(AT_STANFORD_EDU) > -1;
        this.name = name;
        this.email = email;
        this.hashKey = hashKey;
    }

    @Override
    public boolean equals(final Object other) {
        if (other != null && getClass() == other.getClass()) {
            return this.id.equals(((User) other).id);
        }
        return false;
    }

    public String getEmail() {
        return this.email;
    }

    public String getHashedId() {
        if (this.hashedId == null) {
            createHashedId();
        }
        return this.hashedId;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    public boolean isStanfordUser() {
        return this.isStanfordUser;
    }

    private void createHashedId() {
        String[] tokens = this.id.split(AT);
        this.hashedId = new StringBuilder(hash(hash(this.hashKey.concat(tokens[0])))).append('@').append(tokens[1])
                .toString();
    }

    private String hash(final String buffer) {
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bytes = digest.digest(buffer.getBytes(StandardCharsets.UTF_8));
            for (byte element : bytes) {
                sb.append(Integer.toHexString((element & 0xf0) >> 4))
                .append(Integer.toHexString(element & 0x0f));
            }
        } catch (NoSuchAlgorithmException e) {
            throw new LanewebException(e);
        }
        return sb.toString();
    }
}
