# BiologicalData
[![spe-uob](https://circleci.com/gh/spe-uob/BiologicalData.svg?style=shield&circle-token=553606a2ed9bfa7f3c36edbb58a524731ae178ba)](https://circleci.com/gh/spe-uob/BiologicalData)
[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Fspe-uob%2FBiologicalData.svg?type=shield)](https://app.fossa.com/projects/git%2Bgithub.com%2Fspe-uob%2FBiologicalData?ref=badge_shield)

## Description

We are creating an open source web application to speed up the process of transcribing notebooks in Bristol Museum. These notebooks contain important data on species, some of which are now extinct. Currently, transcribing a notebook takes a long time as a transcriber must learn the handwriting of each individual collector and manually transcribe the text into a digital format. 

You can find the website at https://biologicaldata.spe.cs.bris.ac.uk

## Features

* Upload a .jpeg or .png image to be transcribed
* Rotate and zoom into image
* Automatic transcription using Tesseract OCR
* Save and edit transcriptions
* Download transcription as a .txt file

## Build Instructions
Clone BiologicalData
```
git clone https://github.com/spe-uob/BiologicalData.git
```
Move into BiologicalData directory
```
cd BiologicalData
```
Build Jar file
```
mvn clean package
```
Run Jar file
```
java -jar /target/BiologicalData-0.0.1-SNAPSHOT.jar
```
Access in web browser
```
localhost:8080
```
