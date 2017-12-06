import java.net.InetAddress


println("Hello Groovy! From ${java.net.InetAddress.getLocalHost().getHostName()}:${getClass().protectionDomain.codeSource.location.path}")
