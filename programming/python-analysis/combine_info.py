"""
Tool to invoke all Jupyter notebooks in the current directory.

Source: https://medium.com/mlearning-ai/heres-how-to-programmatically-run-all-cells-of-multiple-jupyter-notebooks-f70f32b7091
Author: Martin Thissen; 27.08.2022
"""

from glob import glob
import nbformat
from nbconvert.preprocessors import ExecutePreprocessor

# notebook_list = glob("./*.ipynb")
# correct order is needed:
notebook_list = ["amplitude-analyser.ipynb", "curve-fitter.ipynb"]
ep = ExecutePreprocessor()

for notebook in notebook_list:
    with open(notebook) as notebook_file:
        nb = nbformat.read(notebook_file, as_version=4)
        ep.preprocess(nb)


"""
Merge all PDF outputs into one file.

Library source: https://github.com/py-pdf/pypdf
Documentation (merging PDFs): https://pypdf2.readthedocs.io/en/latest/user/merging-pdfs.html
"""

import os
from PyPDF2 import PdfMerger

summary_filename = "./summarized_plots/output_summary.pdf"
# delete previous summary file to prevent "recursion"
if(os.path.exists(summary_filename)):
    os.remove(summary_filename)

# correct order is needed: is created by numbering pdf files during creation
pdf_list = sorted(glob("./summarized_plots/pdf/*.pdf"))

merger = PdfMerger()

for file in pdf_list:
    merger.append(file)

merger.write(summary_filename)
merger.close()

print('#' * 30 + f'\n{"  Done.  ":#^30}\n' + '#' * 30)
