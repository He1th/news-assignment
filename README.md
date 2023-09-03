# news-assignment
# part 1

The following code has been developed using the Micronaut framework in Java. I chose this language to ensure that my solution aligns with the technology I may encounter in my work. The code comprises a Controller, a service, and a data model. Through the "convertNewsJson" function in the news service and its associated helper functions, it produces the desired output.

## How to run

1. Ensure that you have micronout installed
2. CD into the folder
3. run the following command: ./gradlew run
4. Open localhost:8080

# part 2

The JSON data could be optimized in the following ways:
1. Remove reduntent data by merging connected objects into one object.
2. Objects that should come after each other, should also do so in the JSON object.
3. Start and end time could be calculated on the created JSON Object in my opinion.
