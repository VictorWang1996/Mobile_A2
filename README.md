# Mobile_A2
Instruction: 
This program is implement with minSDKversion 16, and target SDK version is 29.
if you want to change, you can refactor after download the code.
1. In the Android Studio, choose the virtul machine "Google Pixel xl" and select
the API level 29, and refactor the project.
2. Select the "Tools"->"Firebase" to Connect the Firebase, including the Authentication, 
Real-Time Database, and Storage.
3. Syncronize the gradle file, Click the "Run app" button.

App operations:
1. After running, the first page is the "Log In" activity where users can sign with
with an authenticated account or sign up with a new email.
2. If users have signed in, the main activity "Square" will show up, where
users can see others sharings, or choose to send a post by themselves, and
users can like or collect others' posts.
During sending posts, users can choose whether the app can access the location,
and they can choose pictures from phone albums or use the camera.
3. Then, there is also a video page, users can share a video, and customize their preferred
cover for the video.
4. Finally, there is an user page, where the current user can see posts they sent before, and 
edit their information in another activity.
5. In the editing activity, user can edit their head picture, username, age, phone, location and 
click the "save" button.

Criteria:
1. Code Quality: in our code, we have many comments to specify what these functions
are used to do. And we debug lots of time to eliminate unsafe conditions as many as 
possible.
2. Sensors: we implement two sensors, camera and GPS. For the camera, users can take
a photo or take a video upto their choice. The Location function can access the users'
address, including country, state and city. In order to protect privacy, there is a popup
to tell the user whether the user allows the app to access these two services.

3. Connectivity: in this project, we use Google Firebase to implement sign in authentication
and store the data. The user information is stored in Real-Time database, pictures and videos
are stored in Storage database.
4. Responsiveness: as we use the Real-Time database in Firebase, the information can show up
in our activity very soon.
5. Appealing: all the icons and buttons used in our application are very cute and clear which make users
feel happy and know the function directly.
6. Interface: our members design members with care, so that the labels, icons and buttons are very meaningful.
In addition, as the reactiveness, whenever there is a user posting something, the information will show up immediately.
7. Innovation: firstly, when the user wants to share a video, the application allows users to customize their preferred 
cover for the video, which is an interesting and tricky function.
Secondly, when users want to share pictures, the application can modify pictures automatically to fit the screen.
