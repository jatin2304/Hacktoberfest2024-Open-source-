import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import java.net.*;
class RequestProcessor extends Thread
{
private FTServerFrame fsf;
private String id;
private Socket socket;
RequestProcessor(Socket socket, String id,FTServerFrame fsf)
{
this.id=id;
this.fsf=fsf;
this.socket=socket;
start();
}
public void run()
{
try
{
int i,j,x;
byte header[]=new byte[1024];
InputStream is=socket.getInputStream();
OutputStream os=socket.getOutputStream();
int bytesToRead=1024,bytesReadCount=0;
j=0;
i=0;
//receiving header
while(j<bytesToRead)
{
bytesReadCount=is.read(header);
if(bytesReadCount==-1) continue;
j+=bytesReadCount;
}
//parsing header
i=0;
j=1;
int lengthOfFile=0;
while(header[i]!=',')
{
lengthOfFile+=(header[i]*j);
j=j*10;
i++;
}
int lof=lengthOfFile;
i++;
StringBuilder sb=new StringBuilder();
String fileName;
while(header[i]!=' ')
{
sb.append((char)header[i]);
i++;
}
fileName=sb.toString();
SwingUtilities.invokeLater(new Runnable(){
public void run()
{
fsf.updateLog("File to be uploaded: "+fileName+" of length:"+lof);
}
});
//header parsed and sending ack
byte ack[]=new byte[1];
ack[0]=1;
os.write(ack,0,1);
os.flush();
x=0;
int chunkSize=4096;
byte bytes[]=new byte[chunkSize];
File file=new File("uploads"+File.separator+fileName);
if(file.exists()) file.delete();
FileOutputStream fos=new FileOutputStream(file);
while(x<lengthOfFile)
{
bytesReadCount=is.read(bytes);
if(bytesReadCount==-1) continue;
fos.write(bytes,0,bytesReadCount);
fos.flush();
x+=bytesReadCount;
}
fos.close();
ack[0]=1;
os.write(ack,0,1);
os.flush();
socket.close();
SwingUtilities.invokeLater(()->{
fsf.updateLog("File "+fileName+" uploaded on path: "+file.getAbsolutePath());
});
}catch(Exception e)
{
System.out.println(e);
}
}
}
class FTServer extends Thread
{
private ServerSocket serverSocket;
FTServerFrame fsf;
FTServer(FTServerFrame fsf)
{
this.fsf=fsf;
}
public void run()
{
try
{
Socket socket;
RequestProcessor requestProcessor;
serverSocket=new ServerSocket(5500);
while(true)
{
SwingUtilities.invokeLater(()->{
fsf.updateLog("Server ready to accept request on port 5500");
});
socket=serverSocket.accept();
requestProcessor=new RequestProcessor(socket,UUID.randomUUID().toString(),fsf);
}
}catch(Exception e)
{
System.out.println(e);
}
}
public void shutdown()
{
try
{
serverSocket.close();
}catch(Exception e)
{
}
}
}
class FTServerFrame extends JFrame implements ActionListener
{
private JTextArea jTextArea;
private JScrollPane jsp;
private JButton button;
private Container container;
private FTServer server;
private boolean serverState;

FTServerFrame()
{
jTextArea=new JTextArea();
Font jtafont=new Font("Times New Roman",Font.PLAIN,16);
jTextArea.setFont(jtafont);
button=new JButton("start");
jsp=new JScrollPane(jTextArea,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
container=getContentPane();
container.setLayout(new BorderLayout());
container.add(jsp,BorderLayout.CENTER);
container.add(button,BorderLayout.SOUTH);
button.addActionListener(this);
serverState=false;
setLocation(300,300);
setSize(500,500);
setVisible(true);
}
public void actionPerformed(ActionEvent ev)
{
if(serverState==false)
{
server=new FTServer(this);// by this every time when start is clicked a new sever is created
server.start();
serverState=true;
button.setText("stop");
}
else
{
server.shutdown();
serverState=false;
button.setText("start");
jTextArea.append("server closed\n");
}
}
public void updateLog(String msg)
{
jTextArea.append(msg+"\n");
}
public static void main(String args[])
{
FTServerFrame fsf=new FTServerFrame();
}
}
