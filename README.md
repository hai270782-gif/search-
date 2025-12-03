 OptimizedSearchApp – Comprehensive User & Developer Guide

Discover, Compare, and Bookmark Products Effortlessly

1️⃣ What is OptimizedSearchApp?

OptimizedSearchApp is a lightweight Java Swing desktop application designed to help users:

Instantly search for products

Compare prices across multiple e-commerce platforms

Save favorite items and manage search history

It’s fast, secure, and modular, providing a polished user experience without external dependencies.

Highlights:

🔐 Secure login with PBKDF2 hashing + salt

⚡ Non-blocking, high-speed search with multi-threading

🔎 Smart real-time keyword suggestions

⭐ Save, view, and manage bookmarks easily

🔄 Compare prices between any two websites

📂 Automatic keyword history with deduplication & limits

🧱 Modular design: easy to maintain or extend

Runs on Java 11+ and works offline (simulated search data).

2️⃣ System Requirements

JDK 11 or higher

Windows / macOS / Linux

No Internet needed for basic operations

Optional: Internet is only required if you plan to connect APIs or extend functionality.

3️⃣ Getting Started: Source Code

Create a new Java file:

OptimizedSearchApp.java


Copy the full source code provided into the file

Save it in your working directory

4️⃣ Compiling the Application

Open Terminal or CMD in the directory containing the file and run:

javac OptimizedSearchApp.java


If compilation succeeds, no output will appear

Any syntax errors will be reported; check carefully

5️⃣ Running the Application

Run the compiled app with:

java OptimizedSearchApp


The login screen will appear

Ready to explore all features

6️⃣ Login / Registration

Sample account:

Username: testuser
Password: 123456


Or create your own account directly from the interface

Passwords are stored securely with PBKDF2 + salt

Tip: Enable “Remember Me” for automatic login next time.

7️⃣ Key Features & How to Use Them
A. Login Screen

Enter username and password

Option to remember credentials

Link for forgot password instructions

Supports creating new accounts safely

B. Product Search

Enter your query in the Search box

Click Search

Results are shown in a sortable table:

Website

Product name

Price (simulated)

URL

Double-click a row to open the product page

Right-click for options like bookmarking

Note: Search runs in a background thread, keeping the UI responsive.

C. Keyword Suggestions

As you type, the app suggests keywords from previous searches

Click a suggestion to fill the search box automatically

Works offline and improves search speed

Pro Tip: Frequently used keywords appear first, helping you save time.

D. Bookmark Management

Right-click any result → Save Bookmark

Bookmarks saved in:

~/.osa_saved.txt


Access bookmarks via the Bookmarks button

Options include:

View details

Delete individual bookmarks

Copy URL to clipboard

E. Price Comparison

Click Compare Prices

Select two websites
App finds the lowest price for each

Displays price difference clearly

Use Case: Quickly identify the best deal between different platforms.

F. Keyword History

Each search term is stored in:

~/.osa_keywords.txt


Features:

Auto-suggestion for faster searches

Avoids duplicate entries

Limits history to improve performance

8️⃣ Data Storage Overview
File	Purpose
~/.osa_users.txt	Stores user accounts (salt:hash)
~/.osa_saved.txt	Stores bookmarks
~/.osa_keywords.txt	Stores search history
~/.osa_remember.txt	Stores login memory

All files stored under user directory (Windows: C:\Users\<username>\)

9️⃣ Architecture & Design
Component	Responsibility
UserManager	Account registration, login, password security
SearchService	Handles search logic & generates simulated results
KeywordHistory	Stores and suggests keywords
SavedManager	Manages bookmarks
SearchResult	Data model for products
LoginFrame / RegisterDialog	Login UI
MainFrame	Main app UI with search table and price comparison

Design Principle: Modular and easy to extend, separate logic and UI clearly.

🔟 Advantages at a Glance

Non-blocking, fast search

Highly secure login system

Lightweight and cross-platform

Clean, user-friendly interface

Ready to extend to:

Real API connections (Shopee, Lazada, Tiki)

Web scraping functionality

GUI upgrade with JavaFX

🏁 Summary

OptimizedSearchApp is a ready-to-use, fully optimized desktop app that serves both:

End users: quick product search and bookmark management

Developers: a solid base to build extended e-commerce tools

Tips for Developers:

Replace simulated search with API integration

Add more e-commerce platforms dynamically

Enhance UI with JavaFX for richer experience
https://youtu.be/jf9SSZNW_Jo?si=YqbT6IePAaYTHfFL
