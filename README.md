# Checkers

<h1>Rules</h1>

<h5>Starting Position</h5>
- The game consists of two players opposite eachother, each containing 30 checker pieces<br />
- The game is played on a 12x12 board

<h5>Moves</h5>
- All moves are made diagonally<br />
- A player can only move at most one piece per turn unless performing a multi-capture<br />
- Only kings can move backwards<br />
- Flying kings is not allowed in this game

<h5>Captures</h5>
- A piece can be captured if there is a free space opposite of its capturer<br />
- A piece must perform a capture if a capture is present<br />
- A piece cannot capture backwards unless it is a king

<h5>Crowning</h5>
- A piece is crowned once reaching the opposite end of its starting position

<h5>Game Results</h5>
- A draw occurs if both players have only one king left<br />
- A player wins when it has captured all of the opposing teams pieces<br />
- A player wins when its opposing teams pieces can no longer move

<h1>Releases</h1>

<h3>checkers-1.0.1-release November 20 2017</h3>
- Fixed a bug where king checker piece moves were not being properly calculated<br />
- Capturing the opposing king checker piece will no longer award it to you

<h3>checkers-1.0.0-release July 11, 2016</h3>
- This release is the first official version of the game
