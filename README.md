BRIEF
A backend service for the web application for managing classrooms which based on Google Classroom.
SERVICE
This Springboot backend service serve as the main server who are responsible for managing classrooms, account, authentication and authorization of users.
DATABASE
The main data storage of this service is AWS DynamoDB using AWS SDK Enhanced Client for Java 2.x.
AUTHENTICATION
This service use JWT-based authentication method. The request filter chain will authenticate and authorize using JWT token provided.
Requests are authorized base on account participation and role. For example, only accounts joined a classroom can access classroom's detail, or only accounts with role teacher can modify
classroom's participants and rules.
There are 3 roles: STUDENT, TEACHER, and ADMIN.
FUNCTIONS
- Student
    Student can join classrooms, accept invitation by email, and update student ID according to their classroom.
    Student who registered studentId can submit assignments, and watch their scores.
    Student can post comments on their classroom's wall.
    Student can request regrade after they receive their grades.
- Teacher
    Account that create a classroom will automatically assigned as a teacher of the created classroom.
    Teacher can invite students using their emails or remove or hide participants in their classrooms.
    Teacher can access students' assignments and give grades.
- Admin
    Admin can ban or restrict accounts
-Notification
    Students can receive notifications upon receiving grades, posts, or teacher reviews.

