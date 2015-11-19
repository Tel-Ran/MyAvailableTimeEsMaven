package com.mat.interfaces;

import com.mat.json.MatCredential;
import com.mat.json.Scheduler;

public interface ITest {
MatCredential getCredential(int userId, Scheduler scheduler);
}
