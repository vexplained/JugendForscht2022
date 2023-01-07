input_path: str = "sampledata/singles/"
input_filename: str = "1"

averaging_resolution: int = 12

curve_fitter = dict(
    weighting_algorithm = "square", # exp, square, cube
    sign_removal = "abs", # abs, squaring
    precision_pow10 = 0,
    max_pow10 = 5,
)