import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class ReliablePacket {
    private static final int HEADER_SIZE = 9;
    private static final int MAX_PAYLOAD_SIZE = 500;

    private byte [] packetData;

    public ReliablePacket(byte[] payload, int sequnceNumber, boolean isAck) {
        if(payload.length > MAX_PAYLOAD_SIZE) {
            throw new IllegalArgumentException("Payload exceeds maximum size");
        }

        //Construction Header
        byte[] header = new byte[HEADER_SIZE];
        header[0] = (byte) ((sequnceNumber >> 8) & 0xFF); //Sequence number high byte
        header[1] = (byte) (sequnceNumber & 0xFF); //Sequence number low byte
        header[2] = (byte) ((payload.length >> 8) & 0xFF); //Payload length low byte
        header[3] = (byte) (payload.length & 0xFF); //Payload length low byte
        header[4] = (byte) (isAck ? 0x01 : 0x00); //Flag field
        //Compute CRC32 Checksum
        Checksum checksum = new CRC32();
        checksum.update(header, 0, 5); //Compute checksum over header
        checksum.update(payload, 0, payload.length); //Compute checksum over payload
        long checksumValue = checksum.getValue();
        //Convert checksum to bytes
        header[5] = (byte) ((checksumValue >> 24) & 0xFF); //Checksum high byte
        header[6] = (byte) ((checksumValue >> 16) & 0xFF);
        header[7] = (byte) (checksumValue & 0xFF);

        //Concatenate header and payload
        packetData = new byte[HEADER_SIZE + payload.length];
        System.arraycopy(header, 0, packetData, 0, HEADER_SIZE);
        System.arraycopy(payload, 0, packetData, HEADER_SIZE, payload.length);
    }

    public byte[] getPacketData() {
        return packetData;
    }

    public int getSequenceNumber(){
        return((packetData[0] & 0xFF) << 8) | (packetData[1] & 0xFF);
    }

    public int getPayloadLength() {
        return ((packetData[2] & 0xFF) << 8) | (packetData[3] & 0xFF);
    }

    public boolean isAck() {
        return packetData[4] == 0x01;
    }

    public boolean notCorrupt() {
        //Compute checksum and compare with stored checksum
        Checksum checksum = new CRC32();
        checksum.update(packetData, 0, HEADER_SIZE - 4); //Compute checksum over header (excluding checksum field)
        checksum.update(packetData, HEADER_SIZE, getPayloadLength()); //Compute checksum over payload
        long computedChecksum = checksum.getValue();
        long storedChecksum = ((packetData[5] & 0xFF) << 24) | ((packetData[6] & 0xFF) << 16) | ((packetData[7] & 0xFF) << 8) | (packetData[8] & 0xFF);
        return computedChecksum == storedChecksum;
    }
}