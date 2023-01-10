# input_path: str = "sampledata/singles/multiple-occurrences/"
input_path: str = "sampledata/singles/"
input_filename: str = "2"

averaging_resolution: int = 12

curve_fitter = dict(
    weighting_algorithm = "square", # exp, square, cube
    sign_removal = "abs", # abs, squaring
    precision_pow10 = -5,
    max_pow10 = 3,
)