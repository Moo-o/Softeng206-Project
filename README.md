# SOFTENG 206 - EscAIpe Room
Project for SOFTENG 206 (Software Engineering Design 1) in Part II of our Software Engineering degree @The University of Auckland.

# PROJECT VIDEO 
https://github.com/tonylxm/escAIpe-room-game-potioncraft/assets/126369686/1ddb7d3c-7d89-45e7-be2a-f0fe0ace8ab5

# Developers:
- Tony Lim
- Adam Bodicoat
- Andy Zhang

### Built With
- Java
- JavaFX

## Project Requirements
- TODO

## Usage

Prepend every commit message with an emoji with this form:
`<emoji> commit message`.

For commits with multiple types of messages, use multiple lines:
```
<emoji> commit message
<emoji2> commit message2
```

## Which Emoji to Use? ❓

Commit Type | Emoji
----------  | -----
Initial Commit | [🎉 Party Popper](http://emojipedia.org/party-popper/)
Version Tag | [🔖 Bookmark](http://emojipedia.org/bookmark/)
New Feature | [✨ Sparkles](http://emojipedia.org/sparkles/)
Bugfix | [🐛 Bug](http://emojipedia.org/bug/)
Security Fix | [🔒 Lock](https://emojipedia.org/lock/)
Metadata | [📇 Card Index](http://emojipedia.org/card-index/)
Refactoring | [♻️ Black Universal Recycling Symbol](http://emojipedia.org/black-universal-recycling-symbol/)
Documentation | [📚 Books](http://emojipedia.org/books/)
Internationalization | [🌐 Globe With Meridians](http://emojipedia.org/globe-with-meridians/)
Accessibility | [♿ Wheelchair](https://emojipedia.org/wheelchair-symbol/)
Performance | [🐎 Horse](http://emojipedia.org/horse/)
Cosmetic | [🎨 Artist Palette](http://emojipedia.org/artist-palette/)
Tooling | [🔧 Wrench](http://emojipedia.org/wrench/)
Tests | [🚨 Police Cars Revolving Light](http://emojipedia.org/police-cars-revolving-light/)
Deprecation | [💩 Pile of Poo](http://emojipedia.org/pile-of-poo/)
Removal | [🗑️ Wastebasket](http://emojipedia.org/wastebasket/)
Work In Progress (WIP) | [🚧 Construction Sign](http://emojipedia.org/construction-sign/)

## To setup OpenAI's API

- add in the root of the project (i.e., the same level where `pom.xml` is located) a file named `apiproxy.config`
- put inside the credentials that you received from no-reply@digitaledu.ac.nz (put the quotes "")

  ```
  email: "upi123@aucklanduni.ac.nz"
  apiKey: "YOUR_KEY"
  ```
  these are your credentials to invoke the OpenAI GPT APIs

## To setup codestyle's API

- add in the root of the project (i.e., the same level where `pom.xml` is located) a file named `codestyle.config`
- put inside the credentials that you received from gradestyle@digitaledu.ac.nz (put the quotes "")

  ```
  email: "upi123@aucklanduni.ac.nz"
  accessToken: "YOUR_KEY"
  ```

 these are your credentials to invoke GradeStyle

## To run the game

`./mvnw clean javafx:run`

## To debug the game

`./mvnw clean javafx:run@debug` then in VS Code "Run & Debug", then run "Debug JavaFX"

## To run codestyle

`./mvnw clean compile exec:java@style`
