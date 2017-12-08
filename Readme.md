# Sample testing for GoQR 
This tests API from http://goqr.me/api/

Test are far from exhaustive, but is easy to make an idea of how to test parameters 
and how to test more complex combinations of parameters.

## Strategy to followed to test
To test data contained inside QR, the API itself is used (Read QR).
Also, to test colors and background colors, results are tested against samples stored in 
resources folder, pixel by pixel.

## Technology used
To test this API, I used TestNG and gradle to manage dependencies.

## How to run
In a console, at the root of the project simply type
```
./gradlew test --rerun-tasks
``` 