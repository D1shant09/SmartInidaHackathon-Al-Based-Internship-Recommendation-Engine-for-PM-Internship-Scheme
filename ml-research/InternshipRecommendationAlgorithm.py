import numpy as np
import pandas as pd
from sklearn.neural_network import MLPClassifier
from sklearn.preprocessing import MinMaxScaler
from sklearn.model_selection import train_test_split
from sklearn.metrics import roc_auc_score, average_precision_score
from dataclasses import dataclass
# -----------------------------
# 1) Example Dataset (You will replace with real portal data)
# -----------------------------
# Features:
# SkillOverlap (0–1), LocationMatch (0/1), SectorMatch (0/1),
# Rural (0/1), Tribal (0/1), PastParticipation (0/1)
def make_synthetic_pairs(n=5000, seed=42):
 rng = np.random.default_rng(seed)
 skill_overlap = rng.uniform(0, 1, n)
 location_match = rng.integers(0, 2, n)
 sector_match = rng.integers(0, 2, n)
 rural_flag = rng.integers(0, 2, n)
 tribal_flag = rng.integers(0, 2, n)
 past_part = rng.integers(0, 2, n)
 X = np.vstack([
 skill_overlap, location_match, sector_match,
 rural_flag, tribal_flag, past_part
 ]).T.astype(float)
 # "True" label (match=1, not=0) for training
 z = (
 2.0*skill_overlap +
 0.8*location_match +
 0.9*sector_match +
 0.4*rural_flag +
 0.5*tribal_flag -
 0.2*past_part
 )
 p = 1 / (1 + np.exp(-z)) # logistic
 y = (rng.uniform(0, 1, n) < p).astype(int)
 return X, y
# -----------------------------
# 2) Fairness Layer Definition
# -----------------------------
@dataclass
class FairnessWeights:
 w_rural: float = 0.15
 w_no_past: float = 0.10
 w_tribal: float = 0.20
 alpha: float = 0.70 # ML Score weight
 beta: float = 0.30 # Fairness Score weight
def fairness_score(batch_X, fw: FairnessWeights):
 RURAL_IDX, TRIBAL_IDX, PAST_IDX = 3, 4, 5
 rural = batch_X[:, RURAL_IDX]
 tribal = batch_X[:, TRIBAL_IDX]
 no_past = 1 - batch_X[:, PAST_IDX]
 return fw.w_rural*rural + fw.w_no_past*no_past + fw.w_tribal*tribal
def blend_scores(ml_score, fair_score, fw: FairnessWeights):
 return fw.alpha*ml_score + fw.beta*fair_score
# -----------------------------
# 3) Training Phase
# -----------------------------
X, y = make_synthetic_pairs(n=10000, seed=22)
scaler = MinMaxScaler()
X_scaled = scaler.fit_transform(X)
X_train, X_test, y_train, y_test = train_test_split(
 X_scaled, y, test_size=0.2, random_state=1, stratify=y
)
mlp = MLPClassifier(
 hidden_layer_sizes=(64, 32), # 2 hidden layers
 activation='relu',
 solver='adam',
 learning_rate_init=1e-3,
 max_iter=100,
 random_state=1
)
mlp.fit(X_train, y_train)
# -----------------------------
# 4) Evaluation
# -----------------------------
ml_probs = mlp.predict_proba(X_test)[:, 1]
roc = roc_auc_score(y_test, ml_probs)
ap = average_precision_score(y_test, ml_probs)
print(f"ROC-AUC: {roc:.3f}")
print(f"Average Precision: {ap:.3f}")
# -----------------------------
# 5) Apply Fairness + Ranking
# -----------------------------
fw = FairnessWeights()
fair = fairness_score(X_test, fw)
final = blend_scores(ml_probs, fair, fw)
# Rank top 5 student-internship pairs
top5_idx = np.argsort(-final)[:5]
print("\nTop-5 recommendations (Final Score):")
for i, idx in enumerate(top5_idx, 1):
 print(f"{i}. Final={final[idx]:.3f}, ML={ml_probs[idx]:.3f}, Fair={fair[idx]:.3f}, TrueLabel={y_test[idx]}")