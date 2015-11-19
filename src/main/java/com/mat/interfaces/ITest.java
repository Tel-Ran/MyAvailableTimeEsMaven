package com.mat.interfaces;

import com.mat.json.Credential;
import com.mat.json.Scheduler;

public interface ITest {
Credential getCredential(int userId, Scheduler scheduler);
}
