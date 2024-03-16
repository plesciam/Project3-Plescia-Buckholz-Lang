/**
 * Message
 */
public class Message implements ReliableSocket {

        int InetAddress;
        int Message;
        
        public Message(int inetAddress, int message) {
            InetAddress = inetAddress;
            Message = message;
        }
        public int getInetAddress() {
            return InetAddress;
        }
        public void setInetAddress(int inetAddress) {
            InetAddress = inetAddress;
        }
        public int getMessage() {
            return Message;
        }
        public void setMessage(int message) {
            Message = message;
        }
        @Override
        public String toString() {
            return "Message [InetAddress=" + InetAddress + ", Message=" + Message + "]";
        }
        byte[];
    
}