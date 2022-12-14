import cv2
import sys
import os

def detect_save(sourcePath, destinationPath):
    cascPath = "front.xml"
    faceCascade = cv2.CascadeClassifier(cascPath)
    full_image = cv2.imread(sourcePath)
    image = cv2.resize(full_image, (1000, 1000), interpolation = cv2.INTER_AREA)
    
    faces = faceCascade.detectMultiScale(
        cv2.cvtColor(image, cv2.COLOR_BGR2GRAY),
        scaleFactor=1.1,
        minNeighbors=5,
        minSize=(30, 30)
    )

    if(len(faces) != 1):
        return -1
    
    for (x, y, w, h) in faces:
        x1 = int(x-h/1.5)
        y1 = int(y)
        x2 = int(x+h)
        y2 = int(y+w)
        img = image[y1:y2, x1:x2]
        detected = cv2.resize(img, (92, 112), interpolation = cv2.INTER_AREA)
        bw = cv2.cvtColor(detected, cv2.COLOR_BGR2GRAY)
        cv2.imwrite(destinationPath, bw)
        return 1

def process(sourceDirectory, destinationDirectory):
    properCount = 1
    for filename in os.listdir(sourceDirectory):
        f = os.path.join(sourceDirectory, filename)
        if os.path.isfile(f):
            if (detect_save(f, destinationDirectory + "/" + str(properCount) + ".pgm") == 1):
                properCount = properCount + 1
            if (properCount == 11):
                return true
    return false


# cv2.imshow("Detected Face", image)
# cv2.waitKey(0)

# returns true if file is processed succesfully else false
process("/home/samir/Desktop/Project/ImageDetection/images", "/home/samir/Desktop/Project/ImageDetection/detected")