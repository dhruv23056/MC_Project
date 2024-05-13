# Open Bus Transmit

## Team Members:
- Dhruv Patel (MT23056)
- HarshKumar Patel (MT23037)
- Kantilal Patel (MT23063)
- Aryan Joshi (2020036)

## Introduction:
Our Android application aims to revolutionize bus tracking by providing users with real-time updates and comprehensive route information, ensuring a seamless travel experience. The app offers two distinct scenarios: "Inside Bus" for users already on board and "Outside Bus" for those planning their journey. By leveraging GPS technology, users inside the bus can effortlessly determine their current location, view the nearest bus stops, and access the entire route map. For users outside the bus, the app facilitates route planning by offering precise start-to-end navigation, empowering users to manage their time efficiently and stay informed throughout their journey. This report delves into the design, functionality, and implementation of our bus tracking application, highlighting its user-centric approach and the enhanced travel experience it provides.

## User Interface:
The user interface begins with a straightforward sign-in/sign-up process to access the application. Upon successful authentication, users are presented with two prominent buttons: one indicating whether they are inside the bus or outside. For users inside the bus, the interface transitions to a single activity page featuring two lists for selecting source and destination. Additionally, there's a button to capture the user's latitude, longitude, and current time. Once selected, users can view a list of available routes and buses. Clicking on a specific route reveals the path and a list of upcoming stations on Google Maps. This streamlined UI design ensures a user-friendly experience, allowing for easy navigation and access to essential functionalities.

## Functionality:
In real-time bus tracking applications, robust security measures are vital, especially within authentication systems governing user access. This report outlines key aspects of authentication processes in such applications, emphasizing the need for secure mechanisms to protect user data. Authentication Processes:

Authentication processes, including sign-in and sign-up procedures, are pivotal in real-time bus tracking apps. Sign-in verifies existing users through credentials, while sign-up ensures data accuracy and validation. Streamlined workflows and intuitive interfaces enhance the user experience.

- Secure Authentication Implementation: Implementing secure authentication mechanisms like OAuth and Firebase Authentication is crucial. OAuth enables delegated access without exposing sensitive credentials, while Firebase Authentication offers a range of services for enhanced data security.
- Data Protection Measures: Strong adherence to data protection regulations and encryption standards safeguard user privacy and data integrity. Continuous monitoring and access controls detect and mitigate security threats, bolstering user trust.

So, in conclusion, we can say that authentication systems are essential for securing user access in real-time bus tracking applications. By implementing robust authentication mechanisms and upholding data protection standards, organizations can enhance user trust and ensure a secure bus tracking ecosystem.

## Main Dashboard:
The main dashboard of the bus tracking application features two primary buttons: "Inside Bus" and "Outside Bus," allowing users to indicate their location status. Navigation elements provide access to additional features and settings.

- **Inside Bus Scenario:** For users inside the bus, GPS-based location detection captures latitude, longitude, and timestamp data. Interface components facilitate source and destination selection, with options for manual input or predefined choices. A capture button records the current location, while available routes and buses are displayed based on user selection. Detailed route information, integrated with Google Maps, shows the path and upcoming stations, along with real-time updates on bus location and estimated arrival times.
- **Outside Bus Scenario:** Outside the bus, users input source and destination locations for optimal route calculation. Route information is displayed on Google Maps, featuring directions and travel time. Customizable route preferences allow users to tailor their routes based on preferences such as avoiding toll roads or selecting public transit options.
- **Real-time Updates and Notifications:** Continuous updates on bus locations and arrival times ensure users are informed of delays, route changes, and travel updates. Alerts and notifications are provided via push notifications or in-app alerts for user convenience, with customizable notification settings for frequency and types of alerts.
- **User Preferences and Settings:** Personalized features include favorite routes and bus stops, along with accessibility options to cater to diverse user needs.
- **Data Management and Security:** User data is securely stored and encrypted, ensuring compliance with data privacy regulations such as GDPR and CCPA. Data backup and recovery mechanisms further enhance data security.
- **Additional Features:** Integration with external APIs provides access to transit data, mapping services, and route optimization. Search functionality enables users to find buses, routes, and locations efficiently. A feedback and support system encourage user engagement and provides assistance when needed.

## Implementation:
The implementation of our bus tracking application encompasses various scenarios to cater to users' needs comprehensively. Upon authentication, users are directed to the main dashboard, which serves as a gateway to seamless travel management. If a user indicates that they are outside the bus, the application prompts them to select their current stop from a provided list. This selection process ensures accurate route planning and real-time tracking. The chosen stop name is then used to retrieve the corresponding stop ID from the stop.csv file, allowing for precise data handling. Simultaneously, the application captures the user's current time, crucial for determining route availability and estimated travel times. Leveraging this information, the app navigates to the output.csv file, which contains a comprehensive list of routes. By matching the stop ID and current time with route data, the application generates a curated list of available routes for the user's journey. Once the list of routes is displayed, users can select their desired route, triggering a process similar to the "In Bus" scenario. Detailed information about the selected route, including the path on Google Maps, upcoming stations, and estimated arrival times, is presented to the user. This interactive interface empowers users to make informed decisions and plan their journey efficiently. Throughout these processes, our application prioritizes data accuracy, real-time updates, and user-friendly interactions. By seamlessly integrating route planning, real-time tracking, and map navigation, we aim to provide users with a reliable and intuitive travel experience, whether they are inside or outside the bus. Thorough testing and optimization efforts continue to enhance application performance and user satisfaction.

## User Experience:
In evaluating the user experience of the bus tracking application in both scenarios, we have observed several key aspects:

**Inside Bus Scenario:**
- Users appreciate the convenience of GPS-based location detection within the bus, as it provides accurate and real-time information on their whereabouts.
- The interface components for source and destination selection have been well received, offering both manual input and predefined options for enhanced usability.
- Users find the display of available routes and buses based on their selection to be informative and helpful in planning their journeys. The integration with Google Maps for detailed route information has garnered positive feedback, particularly for its clarity and ease of use in navigating upcoming stations.

**Outside Bus Scenario:**
- Users find the manual input for source and destination locations intuitive and straightforward, facilitating easy route planning.
- The optimal route calculation based on user input has been praised for its accuracy and efficiency in providing travel directions and estimated travel time.
- Customizable route preferences have been welcomed by users, allowing them to tailor their routes according to their specific preferences and needs.

**User Feedback and Testing Results:**
Feedback and testing results have played a crucial role in shaping the design and functionality of the bus tracking application. User input has influenced various aspects of the app, including interface design, feature implementation, and usability improvements. Some key points derived from user feedback and
