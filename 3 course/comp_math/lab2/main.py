import math

class IntegralCalculator:
    LEFT_BORDER_OF_GLOBAL_SEGMENT = 5
    RIGHT_BORDER_OF_GLOBAL_SEGMENT = 7

    def __init__(self, precision_scale):
        self.precision_scale = precision_scale
        self.GLOBAL_SEGMENT_LENGTH = self.RIGHT_BORDER_OF_GLOBAL_SEGMENT - self.LEFT_BORDER_OF_GLOBAL_SEGMENT

    def calculate_integrand(self, x):
        cos_value = math.cos(x)
        exponent_value = math.exp(x)
        return exponent_value * cos_value

    def calculate_integral(self, num_of_elementary_segments):
        grid_spacing = self.GLOBAL_SEGMENT_LENGTH / num_of_elementary_segments
        integral = 0
        current_x_coord = self.LEFT_BORDER_OF_GLOBAL_SEGMENT
        for _ in range(num_of_elementary_segments):
            integral += self.calculate_approx_area_on_segment(current_x_coord, current_x_coord + grid_spacing)
            current_x_coord += grid_spacing
        return round(integral, self.precision_scale)

    def calculate_accuracy_of_method_by_runge_rule(self, num_of_elementary_segments):
        first_integral = self.calculate_integral(num_of_elementary_segments)
        second_integral = self.calculate_integral(2 * num_of_elementary_segments)
        third_integral = self.calculate_integral(4 * num_of_elementary_segments)
        sublogarithmic_expression = abs((first_integral - second_integral) / (second_integral - third_integral))
        return round(math.log2(sublogarithmic_expression), self.precision_scale)

    def calculate_approx_area_on_segment(self, left_segment_border_include, right_segment_border_include):
        pass

class TrapezoidalCalculator(IntegralCalculator):
    def calculate_approx_area_on_segment(self, left_segment_border_include, right_segment_border_include):
        assert left_segment_border_include < right_segment_border_include
        grid_spacing = abs(left_segment_border_include - right_segment_border_include)
        return round((self.calculate_integrand(left_segment_border_include) + self.calculate_integrand(right_segment_border_include)) * grid_spacing / 2, self.precision_scale)

class QuadratureFormulaCalculator(IntegralCalculator):
    def calculate_approx_area_on_segment(self, left_segment_border_include, right_segment_border_include):
        assert left_segment_border_include < right_segment_border_include
        actual_grid_spacing = (right_segment_border_include - left_segment_border_include) / 6
        integrand_value_in_x0 = self.calculate_integrand(left_segment_border_include)
        integrand_value_in_x1 = self.calculate_integrand(left_segment_border_include + actual_grid_spacing * 2)
        integrand_value_in_x2 = self.calculate_integrand(left_segment_border_include + actual_grid_spacing * 4)
        integrand_value_in_x3 = self.calculate_integrand(left_segment_border_include + actual_grid_spacing * 6)
        tmp_sum_of_integrand_values = (
            integrand_value_in_x0 * 3 +
            integrand_value_in_x1 * 9 +
            integrand_value_in_x2 * 9 +
            integrand_value_in_x3 * 3
        )
        return round(tmp_sum_of_integrand_values * actual_grid_spacing / 4, self.precision_scale)

class SimpsonCalculator(IntegralCalculator):
    def calculate_approx_area_on_segment(self, left_segment_border_include, right_segment_border_include):
        assert left_segment_border_include < right_segment_border_include
        integrand_value_in_left_border = self.calculate_integrand(left_segment_border_include)
        actual_grid_spacing = right_segment_border_include - left_segment_border_include
        assert actual_grid_spacing > 0
        integrand_value_in_middle = self.calculate_integrand(left_segment_border_include + actual_grid_spacing / 2)
        integrand_value_in_right_border = self.calculate_integrand(right_segment_border_include)
        integrated_parabola_expression = (
            integrand_value_in_left_border +
            integrand_value_in_middle * 4 +
            integrand_value_in_right_border
        )
        return round(actual_grid_spacing * integrated_parabola_expression / 6, self.precision_scale)

def main():
    args = input().split()
    precision_scale = 1000
    if len(args) == 1:
        num_of_elementary_segments = int(args[0])
    elif len(args) == 2:
        num_of_elementary_segments = int(args[0])
        precision_scale = int(args[1])
    else:
        print("Введите количество элементарных отрезков:")
        num_of_elementary_segments = int(input())
        #print("Введите точность знаков после запятой:")
        #precision_scale = int(input())

    print(f"Количество элементарных отрезков на [a,b]: {num_of_elementary_segments}.")
#    print(f"Точность после запятой: {precision_scale}.\n")

    trapezoidal_calculator = TrapezoidalCalculator(precision_scale)
    simpson_calculator = SimpsonCalculator(precision_scale)
    quadrature_formula_calculator = QuadratureFormulaCalculator(precision_scale)

    print(f"Метод трапеции: {trapezoidal_calculator.calculate_integral(num_of_elementary_segments)}")
    print(f"Точность: {trapezoidal_calculator.calculate_accuracy_of_method_by_runge_rule(num_of_elementary_segments)}\n")

    print(f"Метод параболы: {simpson_calculator.calculate_integral(num_of_elementary_segments)}")
    print(f"Точность: {simpson_calculator.calculate_accuracy_of_method_by_runge_rule(num_of_elementary_segments)}\n")

    print(f"Квадратурная формула: {quadrature_formula_calculator.calculate_integral(num_of_elementary_segments)}")
    print(f"Точность: {quadrature_formula_calculator.calculate_accuracy_of_method_by_runge_rule(num_of_elementary_segments)}")

if __name__ == "__main__":
    main()
