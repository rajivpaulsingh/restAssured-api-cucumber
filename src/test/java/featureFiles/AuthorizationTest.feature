Feature: Authorization test for a given user

  Scenario: Authorized user is able to generate jwt and refresh_token
    Given The user with the right credentials
    When Sends the correct authorization request
    Then The user generates the jwt and refresh_token
    And The status code received is '200' success


   Scenario: An unauthorized user is unable to generate jwt and refresh_token
     Given The user with the wrong credentials
     When Sends the wrong authorization request
     Then The user cannot generate the jwt and refresh_token
     And the status code received is '401' Unauthorized