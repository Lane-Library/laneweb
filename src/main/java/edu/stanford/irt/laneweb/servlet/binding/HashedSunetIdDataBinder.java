package edu.stanford.irt.laneweb.servlet.binding;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.SunetIdSource;

public class HashedSunetIdDataBinder implements DataBinder {

    private String sunetidHashKey;

    private SunetIdSource sunetIdSource;

    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        HttpSession session = request.getSession();
        String sessionHashedSunetid = (String) session.getAttribute(Model.HASHED_SUNETID);
        String hashedSunetid = sessionHashedSunetid != null ? sessionHashedSunetid : "";
        String sunetid = this.sunetIdSource.getSunetid(request);
        if (hashedSunetid.isEmpty() && sunetid != null) {
            hashedSunetid = getDigest(getDigest(this.sunetidHashKey + sunetid));
            if (hashedSunetid != null) {
                session.setAttribute(Model.HASHED_SUNETID, hashedSunetid);
            }
        }
        model.put(Model.HASHED_SUNETID, hashedSunetid);
    }

    private String getDigest(final String buffer) {
        StringBuffer sb = new StringBuffer();
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bytes = digest.digest(buffer.getBytes("UTF-8"));
            for (byte element : bytes) {
                sb.append(Integer.toHexString((element & 0xf0) >> 4) + Integer.toHexString(element & 0x0f));
            }
        } catch (UnsupportedEncodingException e) {
            throw new LanewebException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new LanewebException(e);
        }
        return sb.toString();
    }

    public void setSunetidHashKey(final String sunetidHashKey) {
        this.sunetidHashKey = sunetidHashKey;
    }

    public void setSunetIdSource(final SunetIdSource sunetIdSource) {
        this.sunetIdSource = sunetIdSource;
    }
}
