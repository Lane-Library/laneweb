package edu.stanford.irt.laneweb.user;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import edu.stanford.irt.laneweb.LanewebException;

public class User {

    private static final String AT_STANFORD_EDU = "@stanford.edu";

    private String email;

    private String hashedId;

    private String hashKey;

    private String id;

    private String name;

    public User(final String id, final String name, final String email, final String hashKey) {
        // remove @stanford.edu if present for backwards compatibility
        int index = id.indexOf(AT_STANFORD_EDU);
        if (index > -1) {
            this.id = id.substring(0, index);
        } else {
            this.id = id;
        }
        this.name = name;
        this.email = email;
        this.hashKey = hashKey;
    }

    @Override
    public boolean equals(final Object other) {
        if (other instanceof User) {
            return this.id.equals(((User) other).id);
        }
        return false;
    }

    public String getEmail() {
        return this.email;
    }

    public String getHashedId() {
        if (this.hashedId == null) {
            createHashedId(this.hashKey + this.id);
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

    private void createHashedId(final String buffer) {
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bytes = digest.digest(buffer.getBytes(StandardCharsets.UTF_8));
            for (byte element : bytes) {
                sb.append(Integer.toHexString((element & 0xf0) >> 4) + Integer.toHexString(element & 0x0f));
            }
        } catch (NoSuchAlgorithmException e) {
            throw new LanewebException(e);
        }
        this.hashedId = sb.toString();
    }
}
