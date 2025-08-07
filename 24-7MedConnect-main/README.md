# 24-7MedConnect
## Starting the Client and Server
Make sure following npm packages are installed
- `react`
- `express`
- `nodemon`
```bash
To start the server, run the following command at root:

npm run server

To start the client, run the following command at root:

npm run client

you will also have to include a GEMINI_API in .env file in server folder
```

## Inspiration
We were inspired by the challenges many patients face when trying to connect with suitable doctors. Some of the key issues include:

- Difficulty in Finding a Suitable Doctor
- Limited accessibility to a diverse range of doctors
- Language barriers between patients and doctors
- Gender preference for a more comfortable consultation experience

Our goal was to create a platform that addresses these issues, empowering patients with choice and personalized care.

## What It Does
**24/7 MedConnect** offers tailored doctor-patient connections through:

- **AI Integration**: Analyzes symptoms and recommends suitable specialists.
- **Doctor Selection**: Provides patients with a list of available doctors based on their specific symptoms.
- **Personalized Experience**: Allows patients to select doctors based on language and gender preferences for a more comfortable consultation.
- **Secured Live Video Chat**: Enables real-time doctor-patient communication with PeerJS for secure video calls.

## How We Built It

- **Frontend**: Built with **React** for a smooth, user-friendly experience.
- **Backend**: **Node.js** and **Express** handle requests and process data.
- **Database**: **MongoDB** stores doctor profiles and information.
- **AI Integration**: **Gemini API** processes symptoms and suggests appropriate specialties.
- **Real-Time Communication**: **PeerJS** enables secure, live video consultations.

## Challenges We Ran Into

- **Database Connectivity**: We had initial issues connecting **MongoDB** to the backend.
- **PeerJS Integration**: Migrating **PeerJS** for seamless video functionality was challenging.
- **Data Flow**: Ensuring that search results from **MongoDB** were accurately displayed on the frontend required careful management.
- **Database Building**: We needed to manually insert dummy entries into the database before it would work properly.

## Accomplishments We're Proud Of

- Successfully connecting the frontend to **MongoDB**.
- Amazing **UI/UX** design leveraging **Figma** for a modern look.
- Integrating **Gemini API** for AI-driven specialty suggestions and displaying relevant doctor profiles on the frontend.
- Building a functional project with **live video chat** on separate hosts.
- Creating a seamless **sign-up page** for adding new doctors to the database.

## What We Learned

- **Task Prioritization**: We had to prioritize our tasks effectively to achieve our MVP within the hackathon timeframe.
- **Technical Skills**: We learned how to integrate **frontend**, **backend**, and **database** functionality and worked with **PeerJS** for video consultations.
- **Teamwork**: We learned the importance of collaboration and asking questions to overcome roadblocks.

## What's Next for 24/7 MedConnect

- **Doctor Portal**: Build a portal page for doctors to manage their profiles and availability.
- **Enhanced Chat**: Add additional features to the chat page, such as **text chat**, **file uploads**, etc.
- **AI Enhancement**: Improve **AI specialty detection** for even more accurate doctor recommendations.
- **Migrate PeerJS**: Move the **PeerJS** component to the web app for better scalability and management.
