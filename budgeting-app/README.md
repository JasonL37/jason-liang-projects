# A Budgeting Application

## What will the application do?
The application will:
- Allow users to enter their financial activity
- Allow users to create multiple budgets
- Create an account
- Allow users to enter the bills that they need to be paid
  - Keeps track of the days until the deadline for their bill is
- Allow users to keep track of their credit score

## Who will use it?
The people that will use it will be people that want to keep track of their finances.
This is for people that are living on a budget and cannot overspend.
This is for people that have businesses and want to keep track of how 
the business spends money to maximize profits.

## Why is this project of interest to you?
Since I am living on my own, I want to be able to track my financial activity to help better manage my money.
I know many other university students that are also trying to keep track of their expenses to make sure they do not expend over their budget.

## User Stories
As a user, I want to make a budget and add it to my account.
As a user, I want to create an account and view a list of my budgets.
As a user, I want to be able to view my budgets and delete one on my account.
As a user, I want to add my name, income, bills, and credit score to my account.
As a user, I want to view my budget and add savings and expenses to change my budget.

- As a user, when I choose to quit from the application menu, I want to be prompted if I want 
  to save the changes to my account to file.
- As a user, I want to be given the choice to load my previous
  account from file.
- As a user, I want to be able to load my account from a previous file.
- As a user, I want to be able to save my account.

# Instructions for Grader
- You can generate the first required action related to the user story "adding multiple Xs to a Y" by clicking on the button "Add budget to account". This will add a budget to the account.
- You can generate the second required action related to the user story "adding multiple Xs to a Y" by clicking on the button "Remove budget". This will remove a budget from the account.
- You can locate my visual component on the main screen of the user interface.
- You can save the state of my application by clicking on the "Save account to file" button
- You can reload the state of my application by clicking on the "Load account from file" button

# Phase 4: Task 2
Thu Nov 30 15:54:54 PST 2023
Added a budget to an account

Thu Nov 30 15:54:54 PST 2023
Added a budget to an account

# Phase 4: Task 3
I could have added a class for adding a action to a budget. This would have drastically reduced the amount of reused code because a lot of the code in my class responsible for creating an action is
used in multiple classes. By adding a class responsible that creates an action, it would help reduce the redundancy of some of my class, which can help its readability. 
I could have also added a method for setting up the JFrame. This is because this would help its readability and make it easier to understand rather than having a group of methods in the constructor.
I maybe could have created methods in my account for the ToJson methods to reduce the amount of reused code because the code in those ToJson methods have similar code. 
