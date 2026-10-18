
Feature: User Login

  As a user
  I want to log in to Demoblaze
  So that I can access my account

  Scenario: Login with valid credentials
    Given the user opens the Demoblaze website
    When the user logs in with valid credentials
    Then the user should be logged in successfully


  Scenario: Login with invalid credentials
    Given the user opens the Demoblaze website
    When the user logs in with invalid credentials
    Then the login error message should be displayed
