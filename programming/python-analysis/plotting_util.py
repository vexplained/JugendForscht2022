import numpy as np
import pandas as pd
import matplotlib.pyplot as plt

from PyPDF2 import PdfWriter, PdfReader
import io
from reportlab.pdfbase import pdfmetrics
from reportlab.pdfbase.ttfonts import TTFont
from reportlab.pdfgen.canvas import Canvas
from reportlab.lib import pagesizes


# ======== Plotting Util ========

# assign numbers for sorting when combining outputs
export_counter = 1


def plot_amplitude_data(plot_title: str, axis1_name: str, resolution, data1, data1dots: list = [None], axis2_name: str = "", data2: list = [None], data2dots: list = [None], graph_on_same_axis: bool = False, export: bool = True, custom_prefix: str = ""):
    global export_counter

    x = np.linspace(0, len(data1) / resolution, len(data1))

    plt.figure()
    fig, ax = plt.subplots()
    ax.plot(x, data1, "-b", label="data1")
    if len(data1dots) > 1 and data1dots[0] != None:
        ax.plot(x, data1dots, ".", color="#55AAFF", label="data1 dots")
    ax.set_xlabel("Time passed [s]")
    ax.set_ylabel(axis1_name, color="blue")

    # set the x-spine
    ax.spines['left'].set_position('zero')  # type: ignore
    # turn off the right spine/ticks
    ax.spines['right'].set_color('none')
    ax.yaxis.tick_left()

    # set the y-spine
    ax.spines['bottom'].set_position('zero')  # type: ignore

    # turn off the top spine/ticks
    ax.spines['top'].set_color('none')
    ax.xaxis.tick_bottom()

    if len(data2) > 1 and data2[0] != None:
        ax2 = ax
        if not graph_on_same_axis:
            ax2 = ax.twinx()
        ax2.plot(x, data2, "-r", label="data2")
        if len(data2dots) > 1 and data2dots[0] != None:
            ax2.plot(x, data2dots, ".", color='#FFA500', label="data2 dots")
        ax2.set_xlabel("Time passed [s]")
        ax2.set_ylabel(axis2_name, color="red")
    plt.title(plot_title)

    if export:
        name = plot_title.lower().replace(" ", "_")
        plt.savefig(
            f"summarized_plots/png/({custom_prefix}a_{export_counter}){name}.png")
        plt.savefig(
            f"summarized_plots/pdf/({custom_prefix}a_{export_counter}){name}.pdf")
        export_counter += 1
    plt.show()


export_counter = 1


def plot_graph(plot_title: str, axis_name: str, points_x, points_val, graph_x, graph_y, y_axis_limit, export: bool = True, custom_prefix: str = ""):
    """
    Usage example:
    >>> t = np.arange(0, 5, 0.2)
    >>> plot_graph("", "", ..., ..., t, t ** 2)
    """
    global export_counter

    plt.figure()
    fig, ax = plt.subplots()
    ax.plot(points_x, points_val, ".", color="#55AAFF", label="points")
    ax.plot(graph_x, graph_y, "-r", label="function")
    ax.set_ylim(ymax=y_axis_limit)
    ax.set_xlabel("Points [1]")
    ax.set_ylabel(axis_name, color="blue")
    plt.title(plot_title)

    if export:
        name = plot_title.lower().replace(" ", "_")
        plt.savefig(
            f"summarized_plots/png/({custom_prefix}b_{export_counter}){name}.png")
        plt.savefig(
            f"summarized_plots/pdf/({custom_prefix}b_{export_counter}){name}.pdf")
        export_counter += 1
    plt.show()


def plot_4_curves__vs_time(data1, data2, data3, data4, steps_per_second, y_axis_title):
    x1 = np.linspace(0, len(data1) / steps_per_second, len(data1))
    x2 = np.linspace(0, len(data2) / steps_per_second, len(data2))
    x3 = np.linspace(0, len(data3) / steps_per_second, len(data3))
    x4 = np.linspace(0, len(data4) / steps_per_second, len(data4))

    plt.figure()
    fig, ax = plt.subplots()
    ax.plot(x1, data1)
    ax.plot(x2, data2)
    ax.plot(x3, data3)
    ax.plot(x4, data4)
    ax.set_xlabel("Verstrichene Zeit [s]")
    ax.set_ylabel(y_axis_title)
    plt.title(f"{y_axis_title} gegen Zeit")
    plt.show()



def create_pdf_text_page(filename: str, text: str, page_size=pagesizes.landscape(pagesizes.A5)):
    global A5

    # PDF page with info data
    # src: https://stackoverflow.com/a/17538003/19474335

    packet = io.BytesIO()
    cvs = Canvas(packet, bottomup=False, pagesize=page_size)

    # utf-8 encoding support: https://stackoverflow.com/a/17011377/19474335
    pdfmetrics.registerFont(TTFont('Verdana', 'Verdana.ttf'))
    cvs.setFont("Verdana", 11)

    line_height = 15
    y_counter = 2 * line_height
    for line in text.split("\n"):
        cvs.drawString(40, y_counter, line)
        y_counter += line_height
    cvs.save()

    # move to the beginning of the BytesIO buffer
    # packet.seek(0)

    new_pdf = PdfReader(packet)
    with open(filename.replace(".pdf", "") + ".pdf", "wb") as outStream:
        output = PdfWriter()
        output.add_page(new_pdf.pages[0])
        output.write(outStream)

