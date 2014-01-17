package edu.stanford.irt.laneweb.util;

/**
 * A ring buffer of bytes.
 */
public class RingBuffer {

    /** the number of bytes in the buffer */
    private int count;

    /** a byte array holding the byte data */
    private byte[] data;

    /** a pointer to the next avaliable position for input */
    private int inputIndex;

    /** a pointer to the next byte avaliable for output */
    private int outputIndex;

    /**
     * creates a ring buffer of aSize bytes
     * 
     * @param aSize
     *            the number of bytes
     */
    public RingBuffer(final int size) {
        this.data = new byte[size];
    }

    /**
     * makes the buffer empty
     */
    public void clear() {
        this.count = 0;
        this.inputIndex = 0;
        this.outputIndex = 0;
    }

    /**
     * get a byte from the buffer
     * 
     * @return the byte
     * @throws IllegalStateException
     *             ...
     */
    public byte get() {
        if (this.count == 0) {
            throw new IllegalStateException("ring buffer empty");
        }
        byte theByte = this.data[this.outputIndex++];
        if (this.outputIndex == this.data.length) {
            this.outputIndex = 0;
        }
        this.count--;
        return theByte;
    }

    /**
     * accessor for all data in the buffer
     * 
     * @return a byte array of all bytes in the buffer
     */
    public byte[] getBytes() {
        byte[] bytes = new byte[this.count];
        if (bytes.length != 0) {
            int i = 0;
            int j = this.outputIndex;
            while ((j < this.data.length) && (i < this.count)) {
                bytes[i++] = this.data[j++];
            }
            j = 0;
            while (i < this.count) {
                bytes[i++] = this.data[j++];
            }
        }
        return bytes;
    }

    /**
     * used to determine if any data is in the ring buffer
     * 
     * @return true if there is data in the buffer
     */
    public boolean isEmpty() {
        return this.count == 0;
    }

    /**
     * add a byte to the buffer
     * 
     * @param aByte
     *            the byte to add
     * @throws IllegalStateException
     *             ...
     */
    public void put(final byte aByte) {
        if (this.count == this.data.length) {
            throw new IllegalStateException("ring buffer full:" + this.toString());
        }
        this.data[this.inputIndex++] = aByte;
        if (this.inputIndex == this.data.length) {
            this.inputIndex = 0;
        }
        this.count++;
    }

    /**
     * overrides Object.toString()
     * 
     * @return a String representation of this RingBuffer
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("[");
        byte[] bytes = this.getBytes();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(bytes[i]);
            sb.append("(" + (char) bytes[i] + ")");
            if (i != bytes.length - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}