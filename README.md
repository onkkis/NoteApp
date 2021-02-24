# NoteApp
NoteApp made with firebase firestore.

This app uses firestore and firebase email+password authentication.

Document structure is following:

users --> users{username:String, email:String}

user's notes --> users/"username"/notes{text:String,title:String}

specific note from user --> users/"username"/notes/"title"
