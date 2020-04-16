Feature: Agents can search for the best answers

  Background: User generates jwt and refresh_token for authorization
    Given The agent is an authorized user

    Scenario: Authorized user is able to search for best answers
      Given The user with the correct jwt
      When Sends the query for best answer
      Then The user can see the best answer
      And the status code received is '200' success