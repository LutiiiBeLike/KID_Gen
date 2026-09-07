package de.eon.cidgen.dto;

/**
 * JSON returned after a CID has been generated.
 */
public class CidResponse {

    private final String cid;

    public CidResponse(String cid) {
        this.cid = cid;
    }

    public String getCid() {
        return cid;
    }
}
