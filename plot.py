# from matplotlib import pyplot as plt


# im = plt.imread("maze.png")
# implot = plt.imshow(im)

# plt.show()


from calendar import c
import pickle
from this import d
from tkinter import E
from matplotlib import figure, pyplot as plt

point1 = [(845,45)]
point2 = [(1060,125)]
point3 = [(1142,335)]
point4 = [(1083,555)]
point5 = [(870,648)]
point6 = [(660,570)]
point7 = [(556,361)]
point8 = [(630,142)]

fptr = open("cord", "rb")  # open file in read binary mode
cordPath = pickle.load(fptr)  # read binary data from file and store in list
fptr.close()

data = cordPath
a = [p[0] for p in point1]
b = [p[1] for p in point1]

c = [p[0] for p in point2]
d = [p[1] for p in point2]

e = [p[0] for p in point3]
f = [p[1] for p in point3]

g = [p[0] for p in point4]
h = [p[1] for p in point4]

i = [p[0] for p in point5]
j = [p[1] for p in point5]

k = [p[0] for p in point6]
l = [p[1] for p in point6]

m = [p[0] for p in point7]
n = [p[1] for p in point7]

o = [p[0] for p in point8]
p = [p[1] for p in point8]


x = [p[0] for p in data]
y = [p[1] for p in data]
_, ax = plt.subplots()
# ax.set_xticks(range(200))
# ax.set_yticks(range(200))
ax.plot(a, b, 'o-', color='green')
ax.plot(c, d, 'o-', color='green')
ax.plot(e, f, 'o-', color='green')
ax.plot(g, h, 'o-', color='green')
ax.plot(i, j, 'o-', color='green')
ax.plot(k, l, 'o-', color='green')
ax.plot(m, n, 'o-', color='green')
ax.plot(o, p, 'o-', color='green')

ax.plot(x, y, 'o-', color='red')
im = plt.imread("mazesize.jpg")
implot = plt.imshow(im)
plt.show()

