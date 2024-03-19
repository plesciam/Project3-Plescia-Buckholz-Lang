import java.lang.annotation.Annotation;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.type.TypeMirror;

/**
 * Message
 */
 public class Message implements PackageElement
{
        private int Message;
        private int InetAddress;
        private int inetAddress;
        private byte[];

        
            {
             int InetAddress = inetAddress;
            }
            public void getMessage() 
            {
                Message = setMessage(0);
            }

            public int setMessage(int message) {
                this.Message = message;
                return message;
            }

            public void getInetAddress() 
            {
                InetAddress = setInetAddress(0);
                
            }
            
            public int setInetAddress(int inetAddress) {
                this.InetAddress = inetAddress;
                return inetAddress;
            }

            @Override
            public ElementKind getKind() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'getKind'");
            }

            @Override
            public Set<Modifier> getModifiers() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'getModifiers'");
            }

            @Override
            public List<? extends AnnotationMirror> getAnnotationMirrors() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'getAnnotationMirrors'");
            }

            @Override
            public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'getAnnotation'");
            }

            @Override
            public <A extends Annotation> A[] getAnnotationsByType(Class<A> annotationType) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'getAnnotationsByType'");
            }

            @Override
            public <R, P> R accept(ElementVisitor<R, P> v, P p) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'accept'");
            }

            @Override
            public TypeMirror asType() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'asType'");
            }

            @Override
            public Name getQualifiedName() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'getQualifiedName'");
            }

            @Override
            public Name getSimpleName() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'getSimpleName'");
            }

            @Override
            public List<? extends Element> getEnclosedElements() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'getEnclosedElements'");
            }

            @Override
            public boolean isUnnamed() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'isUnnamed'");
            }

            @Override
            public Element getEnclosingElement() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'getEnclosingElement'");
            }
    }
  
    // {
    //         {
                
    //             int setInetAddress(String inetAddress) 
    //             int InetAddress;
    //             int getInetAddress() 
    //             String inetAddress;
    //             String InetAddress = inetAddress;  
                
               
                
    //         }     
            //
    //         public Message(String inetAddress, String message) 
            
    //         private String Message;
    //             Message = message; 
    //         public String getMessage() {}
            
    //         return Message;
    //         }
    //         public void setMessage(String message) 
    //         {
    //         Message = message;
    //         }
          
    //         int byte[];
    //         }
        
        // public Message(int inetAddress, int message) {
        //     InetAddress = inetAddress;
        //     Message = message;
        // }
//         public int getInetAddress() {
//             return InetAddress;
//         }
//         public void setInetAddress(int inetAddress) {
//             InetAddress = inetAddress;
//         }
//         public int getMessage() {
//             return Message;
//         }
//         public void setMessage(int message) {
//             Message = message;
//         }
//         @Override
//         public String toString() {
//             return "Message [InetAddress=" + InetAddress + ", Message=" + Message + "]";
//         }
    
 //}
