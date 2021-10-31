# What is alrt

alrt is an Android app that signals to deaf or hard-of-hearing drivers when there is a siren or car honking in their vicinity.

# Inspiration

While deaf or hard-of-hearing drivers are able to drive with little issue by observing their surroundings, there are some stimuli that can only be heard, including nearby cars honking or the sirens of emergency vehicles. Without external assistance, these signals could be missed, resulting in danger for the driver and those around them.

# What it does

alrt provides noticeable and distinguishable visual cues to help individuals who are deaf or hard-of-hearing to be more cognizant of emergency vehicles and car honks while driving. This is done by utilizing a neural network trained on a wide selection of data to distinguish between normal background sound, car horns, and emergency vehicle sirens. When given an audio stream, the neural network classifies the audio as either a car horn, siren, or children playing (used as a control) along with a confidence level. If the confidence level exceeds a specified threshold for either the car horn or siren, the app will flash accordingly, allowing the driver to take action accordingly

# Core Technologies

alrt was developed using Android Studio, and is powered by a machine learning model trained by [Edge Impulse](https://www.edgeimpulse.com). The C++ code to classify audio is based off of the sample code from the [C++ example](https://github.com/edgeimpulse/example-standalone-inferencing-linux).

# Challenges we ran into

The most difficult part of this project was integrating C++ code into the Android application. There were many issues with the build systems of Android as well as make and cmake. It was also difficult to interface with the Android device and access device sensors such as the microphone. 

# What we learned

The idea for our project arose from a challenge that our group was determined to tackle. Our project required a neural network to be trained, which we had little prior experience with. Then, we had to figure out how to integrate the C++ library produced by the neural network with our Java-based android app using Java JNI. This provided a significant challenge as we had to learn the intricacies of Android Studio while also working with the communication between Java and C++. We tackled these challenges through our combined effors to create a useful and effective project that we are proud to have built.

# What's next for alrt

Although we feel we have accomplished our mission, there is still much we can expand upon. We can offer configuration options to change the audio sensitivity and the display durations, and we hope to have better, more efficient integration of the neural network into the app. We also plan to make alrt operate even more invisibly by operating in the background so that it can be used in conjunction with apps like navigation. Additionally, we plan to continue training the neural network with additional ambient noise so that it can even better distinguish between the target auditiory stimuli and other background noise. Finally, alrt should be integrated with APIs like SmartDeviceLink and SYNC to allow for additional versatility and customizability.
