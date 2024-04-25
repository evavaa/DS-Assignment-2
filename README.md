# DS-Assignment-2

## Basic Features
Multiple users can draw on a shared interactive canvas

Your system will support a single whiteboard that is shared between all clients.

Key Elements with GUI
- Shapes: at least your white board should support for line, circle, oval, and rectangle
- Free draw and erase must be implemented (it will be more convenient if there are
several sizes of eraser)
- Text inputting– allow user to type text anywhere inside the white board
- User should be able to choose their favourite colour to draw the above features. At least 16
colours should be available

## Advanced Features
- Chat Window (text based): To allow users to communicate with each other by typing a text
- A “File” menu with new, open, save, saveAs and close should be provided (only the manager can control this)
- Allow the manager to kick out a certain peer/user

## GUI
### User Login Page
- Users must provide a unique username when joining the whiteboard
- The login page should allow users to choose to create a new whiteboard or join an existing one
- The first user creates a whiteboard and becomes the whiteboard’s
  manager
- Users can join the whiteboard application any time by
  inputting server’s IP address and port number
- A notification will be delivered to the manager if any peer wants to
  join. The peer can join in only after the manager approves
  ◼ A dialog showing “someone wants to share your whiteboard”

### Whiteboard
- When displaying a whiteboard, the client user interface should show the
usernames of other users who are currently editing the same whiteboard
- An online peer list should be maintained and displayed
- The manager
  can kick someone out at any time
- When the manager quits, the application will be terminated. All the
  peers will get a message notifying them

## Development
### Phase 1: Single-user standalone whiteboard

- Implement a client that allows a user to draw all the
expected elements
- Implement a server so that client and server are able to
communicate entities created in Task A

### Phase 2: User management skeleton
- Allow the manager to create a whiteboard
- Allow other peers to connect and join in by getting approval from
the manager
- Allow the manager to choose whether a peer can join in
- join in means the peer's name will appear in the user list
- Allow the joined peer to choose to quit
- Allow the manager to close the application, and all peers get
notified
- Allow the manager to kick out a certain peer/user

### Phases 3: Final
- Integrate the whiteboard with the user
management skeleton (phases 1 and 2)
- Design issues:
  - What communication mechanism will be used? RMI
  - How to propagate the modification from one peer to
other peers? You may need an event-based mechanism 
  - How many threads do we need per peer? At least one for drawing, one for messaging