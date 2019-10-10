package com.example.priorityloadservice.service.fairqueue;

import android.os.Messenger;
import android.os.SystemClock;
import android.util.Log;
import android.util.Pair;

import com.example.priorityfileloadservice.library.Request;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/** WFQ Queue with three priority - Low, Normal and High and type Element - Pair<Request, Message> */
public class LNHProirityFairQueue {

    /** Field - lock for get */
    private Lock lock = new ReentrantLock();

    private static int LOW_STATE = 1;
    private static int NORMAL_STATE = 2;
    private static int HIGH_STATE = 3;

    /** Field - weight for Low Priority Element */
    private static int LOW_PRIORITY_WEIGHT = 1;
    /** Field - weight for Normal Priority Element */
    private static int NORMAL_PRIORITY_WEIGHT = 2;
    /** Field - weight for High Priority Element */
    private static int HIGH_PRIORITY_WEIGHT = 3;

    /** Field - current state */
    private int mCurrentState = HIGH_STATE;
    /** Field - remaining count of elements */
    private int mRemainingItem = HIGH_PRIORITY_WEIGHT;

    /** Field - Low Priority Queue */
    private ConcurrentLinkedQueue<Pair<Request, Messenger>> mLowPriorityQueue;
    /** Field - Normal Priority Queue */
    private ConcurrentLinkedQueue<Pair<Request, Messenger>> mNormalPriorityQueue;
    /** Field - High Priority Queue */
    private ConcurrentLinkedQueue<Pair<Request, Messenger>> mHighPriorityQueue;

    public LNHProirityFairQueue() {
        mLowPriorityQueue = new ConcurrentLinkedQueue<>();
        mNormalPriorityQueue = new ConcurrentLinkedQueue<>();
        mHighPriorityQueue = new ConcurrentLinkedQueue<>();
    }

    /** Add element to FairQueue */
    public boolean offer(Pair<Request, Messenger> aRequestMessengerPair) {
        int currentPriority = aRequestMessengerPair.first.getPriority();
        if (currentPriority == Request.LOW_PRIORITY) {
            return mLowPriorityQueue.offer(aRequestMessengerPair);
        } else if (currentPriority == Request.NORMAL_PRIORITY) {
            return mNormalPriorityQueue.offer(aRequestMessengerPair);
        } else if (currentPriority == Request.HIGH_PRIORITY) {
            return mHighPriorityQueue.offer(aRequestMessengerPair);
        }
        return false;
    }

    /** Get element from FairQueue with delete or null if empty*/
    public Pair<Request, Messenger> poll() {
        lock.lock();

        if(isEmpty()) {
            lock.unlock();
            return null;
        }

        if(mRemainingItem == 0) toNextState();

        if (mCurrentState == HIGH_STATE) {
            if(mHighPriorityQueue.isEmpty()) {
                toNextState();
                lock.unlock();
                return poll();
            } else {
                Pair<Request, Messenger> result = mHighPriorityQueue.poll();
                mRemainingItem--;
                lock.unlock();
                return result;
            }
        }

        if (mCurrentState == NORMAL_STATE) {
            if(mNormalPriorityQueue.isEmpty()) {
                toNextState();
                lock.unlock();
                return poll();
            } else {
                Pair<Request, Messenger> result = mNormalPriorityQueue.poll();
                mRemainingItem--;
                lock.unlock();
                return result;
            }
        }

        if (mCurrentState == LOW_STATE) {
            if(mLowPriorityQueue.isEmpty()) {
                toNextState();
                lock.unlock();
                return poll();
            } else {
                Pair<Request, Messenger> result = mLowPriorityQueue.poll();
                mRemainingItem--;
                lock.unlock();
                return result;
            }
        }

        return null;
    }

    /** return true if FairQueue is empty, false if otherwise */
    public boolean isEmpty() {
        return mHighPriorityQueue.isEmpty() && mLowPriorityQueue.isEmpty() && mNormalPriorityQueue.isEmpty();
    }

    /** return FairQueue size */
    public int size() {
        return mHighPriorityQueue.size() + mNormalPriorityQueue.size() + mLowPriorityQueue.size();
    }

    private void toNextState() {
        if (mCurrentState == HIGH_STATE) {
            mCurrentState = NORMAL_STATE;
            mRemainingItem = NORMAL_PRIORITY_WEIGHT;
        } else if (mCurrentState == NORMAL_STATE) {
            mCurrentState = LOW_STATE;
            mRemainingItem = LOW_PRIORITY_WEIGHT;
        } else {
            mCurrentState = HIGH_STATE;
            mRemainingItem = HIGH_PRIORITY_WEIGHT;
        }
    }

}

