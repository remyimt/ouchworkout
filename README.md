# OuchWorkout

## Introduction
OuchWorkout is an Android application to pratice sport workouts. 8 workouts are available :
* Leg Workout: Squat exercises and other exercises to strengthen your legs.
* Pull Up Workout: Based on three types of pull up. A fixed bar is required.
* Push Up Workout: Push Up exercises and elastic exercises. Elastic bands are required.
* Static Workout: Strengthen your core by holding multiple positions.
* 3 Stretching Workouts: Improve your flexibility.
* With Machines: Strengthen your back in a fitness room.

## Device Compatibility
* Min API required: 16 (Android 4.1 Jelly Bean)
* Min Resolution: 800 x 480 pixels
Although the application should run correcty with higher resolution, I do not test it. Consequently, the
display must be pretty ugly.

## Quick Start
- Start the application and select your workout
![Select your Workout](/screenshots/workout_menu.jpg "Select your Workout")
- Select the exercises of the workout (untick to remove exercises) then click on the 'done' button.
![Select your Exercises](/screenshots/exercise_selection.jpg "Select your Exercises")
- The number of exercises and an approximate time of the workout are displayed. Pictures describe options you
  can configure in the [Settings](#settings) panel. Click on the 'start' button.
![Workout Configuration](/screenshots/workout_configuration1.jpg "Information about the Workout")
- Press the start button (the right arrow) on the top right corner then follow the sign of the top right
  corner. Red sign (rest) means 'keep breathing and relax all of your muscles'.
![Rest Period](/screenshots/rest_time.jpg "Rest Period")
- Green sign (action) means 'just do it'. Execute the exercise.
![Action Period](/screenshots/action_time.jpg "Action Period")

## Settings
The settings panel can be reached by clicking on the 9 small squares on the right top corner of the workout
list:
![Select your Workout](/screenshots/workout_menu.jpg "Select your Workout")
Three options are available:
* Enable Sounds: the application playing sounds before the end of every countdown
* Beep At: The countdown remaining time of the beep sound.
* Manual Exercise Selection: Choose the next exercise of the workout after every exercise
![Settings Panel](/screenshots/settings.jpg "Configure the Settings")
Before starting the workout, settings reminders are displayed. For example, in the following workout, sounds
are disabled and the manual exercise selection is disabled too:
![Workout Configuration](/screenshots/workout_configuration1.jpg "No Sound and Automatic Exercise Selection")
In the next example, the sound is on and the manual exercise selection is enabled:
![Workout Configuration](/screenshots/workout_configuration2.jpg "With Sound and Manual Exercise Selection")

## Advanced Configuration
### Customize exercises
After executing exercises, the after exercise panel proposes to 'review last exercise'. 
![After Exercise Panel](/screenshots/after_time.jpg "After Exercise Panel")
By clicking on this button, you can modify the last exercise to customize the workout for its future execution.
![Review the Last Exercise](/screenshots/review_exercise.jpg "Review the Last Exercise")
At the end of the workout, you have to save the modifications of the workout by clicking on
'save workout modification'.
![Save the Workout Modifications](/screenshots/completed_workout.jpg "Save the Workout Modifications")
NOTE: If you unselect some exercises from the exercise list at the beginning of the workout, these exercises
will not appear in future executions of the workout after saving it.

### Customize workouts
When you start workouts, the workout configuration file is copied to the Download folder of the phone. The
configuration file is a JSON file with the description of exercises. So, you can edit these files to customize
the workout. You can also add new JSON files and put them to the Download/Workouts folder to create new
workouts. The filename must end with '\_wo.json'.
NOTE: Delete the Download/Workouts folder removes all your workout modifications.

### Create workouts
Workouts are defined from JSON files in the application ([Workout Folder](app/src/main/res/raw)) or in the
Download/Workouts folder. When the application starts, every JSON files ending with '\_wo.json' are parsed. A
simple menu allows to select the workout to execute. One workout is described in one JSON file as follows:
```json
{
  "name": "Static Workout",
  "workout": [
    {
      "name": "Push Up",
      "img": "push_up",
      "set_nb": 2,
      "rep_nb": 10,
      "load_kg": 0,
      "action_sec": 20,
      "rest_sec": 30,
      "after_sec": 60
    }, {
      "name": "Leg Press",
      "img": "leg_pres",
      "set_nb": 4,
      "rep_nb": 6,
      "load_kg": 60,
      "action_sec": 10,
      "rest_sec": 30,
      "after_sec": 60
    }
  ]
}
```
Every attribute must be specified.

Description of the workout attributes:
* name: the name of the workout (e.g. 'Static Workout')
* workout: the list of exercises of the workout. Do not forget brackets.

Description of the exercise attributes:
* name: the name of the exercise (e.g. 'Push Up')
* img: the image of the exercise (e.g. 'push\_up'). The exercise images (with PNG extensions) are included in
  the [Picture Folder](app/src/main/res/drawable).
* set\_nb: the number of repetitions of both the action phase and the rest phase.
* rep\_nb: the number of repetitions of the exercise during the action phase.
* load\_kg: the load used for the exercise. The load is not displayed if the load is equals to 0.
* action\_sec: the period (action phase) in seconds to execute the rep\_nb repetitions of the exercise .
* rest\_sec: the period (rest phase) in seconds to recover before starting another action phase.
* after\_sec: the period (after phase) in seconds to recover before starting the next exercise.

The above workout included two exercises: push up and leg press. You have 20 seconds to execute 10 push ups
then you rest for 30 seconds before executing 10 push ups again. Then you rest for 60 seconds and doing the
leg press exercise with 60 kg.

## Install the application

### From Android Studio
NOTE: The application does not exist on the Playstore :'(

* To install the application and add or customize workouts, you can download 
Android Studio and install it: https://developer.android.com/studio/index.html
* Download the OuchWorkout project and open it in Android studio
* Then, connect your Android device and 'Run the app' to install the application on the device:
https://developer.android.com/training/basics/firstapp/running-app.html

### From the .apk file
* Allow the installation of packages from unknown sources (Settings > Security > Unknwown sources). Read the
  code of the application to check it does not corrupt your system :P
* Download the .apk package: [OuchWorkout package](app/release/app-release.apk)
* Put the package on your android device
* Click on the package and install it
* Disable "Unknwon sources" installation

