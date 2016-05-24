
/*

ACTORS!


REFERENCES
TODO:  Akka Streams replacing Iteratees and Enumerators?
- p149 Manning Reactive Web Apps
- http://letitcrash.com


ACTOR
- INTERNAL mutable state
- send-receive messages
- change its own state in response to message arriving
- start new "child: actors

WHEN TO USE FUTURES vs ACTORS
- FUTUREs for concurrency; one-off async task that will complete; i.e. to get results from a DB; or from a Web Service
- ACTORs for deamon long-running process to handle state-transitions; or make decisions based on past state
ie can change their own state based on messages past


 */

/*

 USING PLAY with Acivator (Http Web Server)
 ******************************************

    - Section 2.1; p28
    - run in DEV mode with auto-updated runtime on source changes; via 'activator run' where build.sbt file is

    - Ctrl+D to get into SBT console; OR just start in SBT console via 'activator' without run cmd

    - goto http:/localhost:9000 to see App
    *** Play Tutorial is DEFAULT display in (twitter-stream) project template
    - instructions for using IntelliJ with Play here:
    http://localhost:9000/@documentation/IDE

    *** use template for (twitter-stream)
    - open Project folder in IntelliJ IDEA; and choose defaults to generate IntelliJ project!
    BUT get WARNING messages on tyrex ... etc.;  NOTE that no 'src' directory got generated in the template!
    - Play WebApp entrypoint is within ./conf/routes file!
    - which maps to HomeController.index to an Action
    - which references views/html template to display!

    - NOTE: Scala code with @ is EMBEDDED into this; and @ references Scala objects for templates, etc
      CAN embed custom HTML in those templates, along with Scala simple operations!

    ***** MODIFY code to account for parse-bug!


 */

/*

  ENUMERATOR/ITERATEE
  - Enumerator provides SOURCE of data
  - Enumeratee provides FILTER for data
  - Iteratee provides

 */

/*

    TWITTER TOKENS!

    API Key:
    	Zzpuunm157uJBJTgNa0u5BkWV

    API Secret:
      FTcolBMVQAsMkJ6fTCo6FvrPg4p5rv4JMyr2n8NhVLdinHLNus

    Access Token:
      250322617-c2fKwLVcKa1lndsjV76wtvafItI8yg1mTttvFzwz

    Access Token Secret:
      wV9AhFw7uik7WvSZsCFMmbiies1xySdxSI372v8UDNcHt

 */

