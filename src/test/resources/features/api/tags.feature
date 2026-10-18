
@api
Feature: Tags API

  Scenario: Get list of tags successfully
    Given the API request is configured
    When the client requests the tags endpoint
    Then the API response status code should be 200
    And the response should contain a list of tags
