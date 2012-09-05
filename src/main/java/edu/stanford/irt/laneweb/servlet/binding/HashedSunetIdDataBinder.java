package edu.stanford.irt.laneweb.servlet.binding;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

//TODO: combine this into SunetIdAndTicketDataBinder
public class HashedSunetIdDataBinder implements DataBinder {

    private String sunetidHashKey;

    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        HttpSession session = request.getSession();
        String sessionHashedSunetid = (String) session.getAttribute(Model.AUTH);
        if (sessionHashedSunetid == null) {
            String sunetid = ModelUtil.getString(model, Model.SUNETID);
            if (sunetid != null) {
                String hashedSunetid = getDigest(getDigest(this.sunetidHashKey + sunetid));
                session.setAttribute(Model.AUTH, hashedSunetid);
                model.put(Model.AUTH, hashedSunetid);
            }
        } else {
            model.put(Model.AUTH, sessionHashedSunetid);
        }
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
}
