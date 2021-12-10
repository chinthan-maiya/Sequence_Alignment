import matplotlib.pyplot as plt
import numpy as np

input_size = np.array([128, 256, 512, 768, 1024, 2048, 4096, 8192, 16384])

cpu_time1 = np.array([51, 68, 95, 132, 180, 197, 228, 497, 1966])
memory1 =  np.array([1048, 1551, 4194, 7340, 14134, 17013, 59579, 115562, 963073])

cpu_time2 = np.array([126, 146, 181, 201, 218, 362, 427, 862, 2207])
memory2 = np.array([2097, 3649, 8388, 14680, 23068, 45874, 37739, 27961, 31835])

def plot_cpu_time():
    plt.plot(input_size, cpu_time1, label='Basic')
    plt.plot(input_size, cpu_time2, label='Efficient')
    plt.xlabel("Input size")
    plt.ylabel("CPU time in milliseconds")
    leg = plt.legend(loc='upper center')
    plt.show()

def plot_memory():
    plt.plot(input_size, memory1, label='Basic')
    plt.plot(input_size, memory2, label='Efficient')
    plt.xlabel("Input size")
    plt.ylabel("Memory in KB")
    leg = plt.legend(loc='upper center')
    plt.show()

plot_memory()
plot_cpu_time()