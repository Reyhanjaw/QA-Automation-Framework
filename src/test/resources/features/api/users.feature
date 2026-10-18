
@api
Feature: Get Users API

  Scenario: Get users successfully
    Given the API request is configured
    When the client requests the users endpoint
    Then the API response status code should be 200
    And the response should contain a list of users

  Scenario: Get user by ID successfully
    Given the API request is configured
    When the client requests an existing user by ID
    Then the API response status code should be 200
    And the response user ID should match the requested ID

  Scenario: Create user successfully
    Given the API request is configured
    When the client creates a new user
    Then the API response status code should be 200
    And the created user should contain an ID and the submitted data


  Scenario: Update user successfully
    Given the API request is configured
    When the client creates a new user
    And the client updates the user's first name
    Then the API response status code should be 200
    And the updated user's first name should match the request


  Scenario: Delete user successfully
    Given the API request is configured
    When the client creates a new user
    And the client deletes the created user
    Then the API response status code should be 200
