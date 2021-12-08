import matplotlib.pyplot as plt
import numpy as np

input_size = np.array([128, 256, 512, 768, 1024])

cpu_time1 = np.array([54, 73, 122, 244, 421])
memory1 =  np.array([1048, 1551, 4194, 7340, 14134])

cpu_time2 = np.array([0, 0, 0, 0, 0])
memory2 = np.array([0, 0, 0, 0, 0])

def plot_cpu_time():
    plt.plot(input_size, cpu_time1)
    plt.plot(input_size, cpu_time2)
    plt.xlabel("Input size")
    plt.ylabel("CPU time in milliseconds")
    plt.show()

def plot_memory():
    plt.plot(input_size, memory1)
    plt.plot(input_size, memory2)
    plt.xlabel("Input size")
    plt.ylabel("Memory in KB")
    plt.show()

plot_memory()
plot_cpu_time()