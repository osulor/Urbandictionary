# Urbandictionary
This app connects to the Urban Dictionary API. The app allows user to enter a word they wish to search 
and the app display the different definitions of the word in a RecyclerView with number of thumbs up and down.
The user can sort the definition by most thumps up or down.

The app follows the MVVM architecture.

Tech Stack:
  - Retrofit + Rxjava for asynchronous calls.
  - Koin for dependency injection.
  - Unit Testing with MocKk for mocking dependencies.
  - UI testing with Espresso.
  
  Important: Make sure to run the UI test on the mockDebug build variant and not the prodDebug. Since it's possible to have some data change in the API it's crucial to mock the API data in order to check if the UI behave as expected.
  
 
