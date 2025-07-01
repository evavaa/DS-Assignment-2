# Distributed Shared Whiteboard

<img width="912" alt="ds-1" src="https://github.com/user-attachments/assets/6e75a193-4f9e-439d-8f61-61bf69cce3f3" />

<img width="766" alt="ds-2" src="https://github.com/user-attachments/assets/b0682631-d02f-45c5-bf36-8b6bad3c2cfc" />



## Features

### Drawing & Editing
- **Shapes**: Line, circle, oval, rectangle
- **Free draw and erase** with multiple eraser sizes
- **Text input** anywhere on the whiteboard
- **Color selection** with 16+ available colors

### Collaboration
- **Real-time sharing** between multiple users
- **Text-based chat** for user communication
- **User management**: Manager can approve/reject users and kick out peers
- **File operations**: New, open, save, saveAs, close (manager only)

### User Interface
- **Login system**: Unique usernames, create or join whiteboards
- **User list**: Display active users and their status
- **Manager privileges**: First user becomes manager with full control
- **Network joining**: Connect via IP address and port
- **Approval system**: Manager must approve new users

## How to Run

This application consists of three main components: a server and two types of clients. Follow these steps to run the distributed whiteboard:


### Step 1: Start the Whiteboard Server
```bash
java -jar WhiteboardServer.jar [port]
```
- `port`: Port number for the server
- Example: `java -jar WhiteboardServer.jar 1099`


### Step 2: Create a Whiteboard (Manager)
```bash
java -jar CreateWhiteBoard.jar [serverIP] [serverPort] [username]
```
- `serverIP`: IP address of the server (use `localhost` or `127.0.0.1` if running locally)
- `serverPort`: Port number the server is running on
- `tcpPort`: Port number for the TCP management server (must be different from the main server port, used for user administration)
- `username`: Your unique username as the manager
- Example: `java -jar CreateWhiteBoard.jar localhost 1099 3000 Manager1`

The whiteboard manager can:
- Control file operations (new, open, save, saveAs, close)
- Approve/reject users joining the whiteboard
- Kick out other users

### Step 3: Join an Existing Whiteboard (Regular Users)
```bash
java -jar JoinWhiteBoard.jar [serverIP] [serverPort] [username]
```
- `serverIP`: IP address of the server
- `serverPort`: Port number the server is running on  
- `tcpPort`: Port number for the TCP server
- `username`: Your unique username
- Example: `java -jar JoinWhiteBoard.jar localhost 1099 3000 User1`

**Note**: Regular users need approval from the manager to join the whiteboard.

### Running on Multiple Machines
1. Start the server on one machine and note its IP address
2. Replace `localhost` with the actual IP address when running clients from other machines
3. Ensure firewall settings allow connections on the specified port

### Troubleshooting
- Make sure Java is installed and accessible from command line
- Verify the server is running before starting clients
- Check network connectivity if running across multiple machines
- Ensure usernames are unique for each user
