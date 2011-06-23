package edu.stanford.irt.laneweb.util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import edu.stanford.irt.search.impl.specials.socrates.RingBuffer;

public class BasePathSubstitutingInputStream extends FilterInputStream {

    private static final byte DOT = '.';

    private static final byte SLASH = '/';

    private byte[] basePathArray;

    private int basePathIndex;

    private boolean doneReading = false;

    private RingBuffer ringBuffer;

    private boolean substituting = false;

    public BasePathSubstitutingInputStream(final InputStream in, final String basePath) {
        super(in);
        this.basePathArray = basePath.getBytes();
        this.ringBuffer = new RingBuffer(5);
    }

    @Override
    public void mark(final int readlimit) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean markSupported() {
        return false;
    }

    @Override
    public int read() throws IOException {
        if (this.substituting) {
            if (this.basePathIndex >= this.basePathArray.length) {
                this.substituting = false;
            } else {
                return this.basePathArray[this.basePathIndex++];
            }
        }
        if (this.ringBuffer.isEmpty()) {
            if (this.doneReading) {
                return -1;
            }
            int input = super.in.read();
            if (input == -1) {
                this.doneReading = true;
                return -1;
            }
            byte theByte = (byte) input;
            this.ringBuffer.put(theByte);
            if (theByte == SLASH) {
                input = super.in.read();
                theByte = (byte) input;
                this.ringBuffer.put(theByte);
                if (theByte == DOT) {
                    input = super.in.read();
                    theByte = (byte) input;
                    this.ringBuffer.put(theByte);
                    if (theByte == SLASH) {
                        input = super.in.read();
                        theByte = (byte) input;
                        this.ringBuffer.put(theByte);
                        if (theByte == DOT) {
                            input = super.in.read();
                            theByte = (byte) input;
                            this.ringBuffer.put(theByte);
                            if (theByte == SLASH) {
                                this.ringBuffer.clear();
                                this.substituting = true;
                                this.basePathIndex = 0;
                                return this.basePathArray[this.basePathIndex++];
                            }
                        }
                    }
                }
            }
        }
        return this.ringBuffer.get();
    }

    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        int i;
        int input;
        for (i = off; i < len + off; i++) {
            input = this.read();
            if (input == -1) {
                if (i == off) {
                    return -1;
                }
                break;
            } else {
                b[i] = (byte) input;
            }
        }
        return i - off;
    }

    @Override
    public void reset() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long skip(final long n) throws IOException {
        long l;
        for (l = 0; l < n; l++) {
            if (this.read() == -1) {
                break;
            }
        }
        return l;
    }
}
