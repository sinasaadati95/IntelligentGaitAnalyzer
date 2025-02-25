# Artificial Neural Networks

In this project, an IoT-based methodology is utilized to digitize human motion during the gait cycle, capturing movement signals from three lower body joints: the ankle, knee, and thigh. The data are published in the "Datasets.zip". Artificial neural networks are then developed and trained to detect normal and abnormal motions. The results demonstrate that the application can successfully train and detect normal and abnormal motions in each lower body joint.

### Datasets Structure and Methodology

The data in this project are designed such that each row summarizes the motion of one joint during multiple gaits of one person. Each row is a vector containing 20 floating-point values representing the angles of the joint during the gait.

To enhance the evaluation process, the number of normal and abnormal cases is balanced for each joint. Due to the limited number of patients and clinical constraints, data augmentation methodologies are employed. The Python code for some experiments is published in the "Phase1" and "Phase2" directories, where details of neural network development, data augmentation, and other necessary information can be found.
