package de.eon.cidgen.service;

/** Thrown when every four-character base-34 value in a CID range was used. */
public class CidRangeExhaustedException extends RuntimeException {

    public CidRangeExhaustedException(String rangeLetter) {
        super("CID range " + rangeLetter + " is exhausted");
    }
}
