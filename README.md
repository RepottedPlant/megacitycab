# Mega City Cab v1.0 Release

**Release Date:** March 13, 2025

## Overview
This is the initial v1.0 release of the Mega City Cab Online Vehicle Reservation System. Developed as part of the CIS6003 Advanced Programming assignment, this release transforms the manual cab reservation process into a robust, web-based solution. The system streamlines customer bookings, billing, fleet management, and reporting—all built with modularity and scalability in mind.

## Key Features
- **User Authentication:**  
  Secure login with username and password, including session management and access control.
- **Booking Management:**  
  - Create, view, search, and manage customer bookings.  
  - Automatic fleet assignment and dynamic pricing calculation using Standard, Discount, and Peak pricing strategies.
- **Billing:**  
  Calculation of base fare, tax (12%), and total amounts based on booking details and vehicle type.  
  Integrated with a dedicated Billing DAO for data persistence.
- **Customer, Fleet, and User Management:**  
  CRUD operations for customers, fleet vehicles, and user accounts with comprehensive input validation and error handling.
- **Reporting:**  
  Detailed reports on total bookings, revenue, breakdowns by pricing strategy, vehicle type, and driver performance.
- **Design Patterns Implementation:**  
  Utilizes DAO, Strategy, and Observer patterns to ensure maintainability, reusability, and scalability.
- **Distributed Architecture:**  
  Developed as a web application using Java Servlets, JSPs, and deployed on an Apache Tomcat server. The architecture supports future RESTful extensions.
- **Testing and Automation:**  
  Extensive service layer testing using JUnit and a TDD approach, with automated tests integrated into the CI pipeline.

## Documentation & Version Control
- **Design Documentation:**  
  Includes comprehensive UML diagrams (Use Case, Class, Sequence) and detailed design rationales.
- **Test Documentation:**  
  Full test plans, test cases, and automated testing setup for service validation.
- **Version Control:**  
  Managed on GitHub with clear branching strategies, commit histories, and deployment workflows.

## Known Issues and Future Enhancements
- **Scalability:**  
  Future updates may include a RESTful API to support mobile applications and additional integrations.
- **UI Enhancements:**  
  Planned improvements for enhanced responsiveness and advanced reporting dashboards.
- **Security:**  
  Further layers of encryption and refined secure coding practices will be implemented in future releases.

## Getting Started
1. **Clone the Repository:**
   ```bash
   git clone https://github.com/yourusername/MegaCityCab.git
1. **Follow the Setup Instructions:**
  Refer to the README for detailed installation and configuration instructions.
1. **Clone the Repository:**
   Deploy the application on Apache Tomcat or your preferred Java EE server.
