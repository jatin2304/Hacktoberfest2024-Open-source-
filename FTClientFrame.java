import java.io.*;
import java.net.*;
class FTClientUploader extends Thread
{
public void run()
{
try
{
String fileName=args[0];
File file=new File(fileName);
if(!file.exists()) 
{
System.out.println(fileName+" does not exist.");
return;
}
if(file.isDirectory())
{
System.out.println(fileName+" is a directory.");
return;
}
long lengthOfFile=file.length(); 
System.out.println(lengthOfFile);
byte header[]=new byte[1024];
int i,j;
i=0;
long x=lengthOfFile;
while(x>0)
{
header[i]=(byte)(x%10);
x=x/10;  
i++;
}
header[i]=(byte)',';
i++;
j=0;
while(j<fileName.length())
{
header[i]=(byte)fileName.charAt(j);
j++;
i++;
}
while(i<=1023)
{
header[i]=(byte)' ';
i++;
}
//header generated
Socket socket=new Socket("localhost",5500);
InputStream is=socket.getInputStream();
OutputStream os=socket.getOutputStream();
int bytesToSend=1024;
os.write(header,0,bytesToSend); //header sent
os.flush();
//waiting for acknowledgement
byte ack[]=new byte[1];
int bytesReadCount;
while(true)
{
bytesReadCount=is.read(ack);
if(bytesReadCount==-1) continue;
break;
}
//ack received
FileInputStream fis=new FileInputStream(file);
byte bytes[]=new byte[4096];
x=0;
//sending file
while(x<lengthOfFile)
{
bytesReadCount=fis.read(bytes);
os.write(bytes,0,bytesReadCount);
os.flush();
x+=bytesReadCount;
}
fis.close();
//ack of data file
while(true)
{
bytesReadCount=is.read(ack);
if(bytesReadCount==-1) continue;
break;
}
socket.close();
System.out.println(fileName+" uploaded");
}catch(Exception e)
{
System.out.println(e);
}
}
}
class FTClientFrame extends JFrame
{
private String id;
private String host;
private Container container;
private JScrollPane jsp;
private FileUploadViewPanel fileUploadViewPanel;
private FileSelectionPanel fileSelectionPanel;
private FTClientFrame fcf;
FTClientFrame(String id,String host,FTClientFrame fcf)
{
this.id=id;
this.host=host




}
public class Panel
public static void main(String args[])
{
FTCLientFrame fcf=new FTClientFrame();
}
}