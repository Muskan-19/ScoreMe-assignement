import random
import json
import os


def generate_instance(n, K, d=4, conflict_density=0.3, seed=42):
    """ Generate a random MSME Credit Pipeline Scheduling instance. """
    random.seed(seed)
    tasks = [f'T{i}' for i in range(n)]
    conflicts = [(i, j) for i in range(n) for j in range(i + 1, n)
                 if random.random() < conflict_density]
    cap = [32, 128, 8, 6.0]                          
    resources = [[random.uniform(1, cap[d] // (n // K + 1))
                  for d in range(4)] for _ in range(n)]
    capacities = [cap[:] for _ in range(K)]
    windows = [(lo := random.randint(0, K - 2),
                random.randint(lo + 1, K - 1)) for _ in range(n)]
    weights = [random.uniform(1, 10) for _ in range(n)]
    return dict(tasks=tasks, conflicts=conflicts,
                resources=resources, capacities=capacities,
                windows=windows, weights=weights, K=K)


                                              
BENCHMARKS = [
                           
    (8,   3,  0.30, 1),
    (10,  4,  0.40, 2),
    (12,  4,  0.50, 3),
    (50,  8,  0.25, 10),
    (100, 10, 0.30, 11),
    (150, 12, 0.35, 12),
    (200, 15, 0.40, 20),
    (200, 5,  0.60, 21),
    (200, 20, 0.10, 22),
]

if __name__ == "__main__":
    os.makedirs("benchmark", exist_ok=True)
    for n, K, density, seed in BENCHMARKS:
        instance = generate_instance(n=n, K=K, conflict_density=density, seed=seed)
                                                                       
                                                                              
        filename = f"benchmark/n{n}_K{K}_d{density}_seed{seed}.json"
        with open(filename, "w") as f:
            json.dump(instance, f)
        print(f"wrote {filename}")
