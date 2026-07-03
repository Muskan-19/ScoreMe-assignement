import csv
import matplotlib.pyplot as plt

rows = []
with open("benchmark_results.csv") as f:
    for row in csv.DictReader(f):
        if row["status"] == "OK":
            rows.append(row)

rows.sort(key=lambda r: int(r["n"]))

def category(label):
    return label.split("-")[0]

color_map = {"small": "#2E86AB", "medium": "#F6A623", "stress": "#D64545"}

                               
fig, ax = plt.subplots(figsize=(8, 5.5))
xs = [int(r["n"]) for r in rows]
ys = [float(r["penalty"]) for r in rows]
ax.plot(xs, ys, color="grey", alpha=0.5, linestyle="--", zorder=1)
for r in rows:
    c = color_map[category(r["label"])]
    ax.scatter(int(r["n"]), float(r["penalty"]), color=c, s=90, zorder=3,
               edgecolor="white", linewidth=0.8)
    ax.annotate(f"{r['label']}\n({r['tasks_assigned']}/{r['tasks_total']} assigned)",
                (int(r["n"]), float(r["penalty"])), fontsize=7,
                textcoords="offset points", xytext=(6, 6))
handles = [plt.Line2D([0], [0], marker='o', color='w', markerfacecolor=c, markersize=9, label=cat)
           for cat, c in color_map.items()]
ax.legend(handles=handles, title="Instance class")
ax.set_xlabel("Number of tasks (n)")
ax.set_ylabel("Total penalty (partial, since RPLBS reports infeasible on all 9)")
ax.set_title("Penalty vs Number of Tasks (n)")
ax.grid(alpha=0.3)
fig.tight_layout()
fig.savefig("chart_penalty_vs_n.png", dpi=150)
plt.close(fig)

                               
fig, ax = plt.subplots(figsize=(8, 5.5))
ys2 = [float(r["runtime_ms"]) for r in rows]
ax.plot(xs, ys2, color="grey", alpha=0.5, linestyle="--", zorder=1)
for r in rows:
    c = color_map[category(r["label"])]
    ax.scatter(int(r["n"]), float(r["runtime_ms"]), color=c, s=90, zorder=3,
               edgecolor="white", linewidth=0.8)
    ax.annotate(r["label"], (int(r["n"]), float(r["runtime_ms"])), fontsize=8,
                textcoords="offset points", xytext=(6, 6))
ax.legend(handles=handles, title="Instance class")
ax.set_xlabel("Number of tasks (n)")
ax.set_ylabel("Runtime (ms, median of 5 runs)")
ax.set_title("Runtime vs Number of Tasks (n)")
ax.grid(alpha=0.3)
fig.tight_layout()
fig.savefig("chart_runtime_vs_n.png", dpi=150)
plt.close(fig)

print("Wrote chart_penalty_vs_n.png and chart_runtime_vs_n.png")
