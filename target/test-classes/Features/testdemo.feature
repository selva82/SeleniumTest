@tag
Feature: Feature to test WishList cart

  @tag1
  Scenario: Add four different products to the cart
    Given open browser
    Given I want to add four different products to my wishlist
    When I view my wishlist table
    Then I find total four selected items in my wishlist
    When I search for lowest price product
    And I am able to add the lowest price item to my cart
    Then I am able to verify the item in my cart
