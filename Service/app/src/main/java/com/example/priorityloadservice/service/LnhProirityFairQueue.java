package com.example.priorityloadservice.service;

import com.example.priorityfileloadservice.library.Priority;

import java.util.ArrayDeque;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/** WFQ Queue with three priority - Low, Normal and High and type Element - Priority */
public class LnhProirityFairQueue {

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
    private ArrayDeque<Priority> mLowPriorityQueue;
    /** Field - Normal Priority Queue */
    private ArrayDeque<Priority> mNormalPriorityQueue;
    /** Field - High Priority Queue */
    private ArrayDeque<Priority> mHighPriorityQueue;

    /** Class's constructor */
    public LnhProirityFairQueue() {
        mLowPriorityQueue = new ArrayDeque<>();
        mNormalPriorityQueue = new ArrayDeque<>();
        mHighPriorityQueue = new ArrayDeque<>();
    }

    /**
     * Add element to FairQueue
     *
     * @param aPriority - elem what added
     */
    public boolean add(Priority aPriority) {
        int currentPriority = aPriority.getPriority();
        if (currentPriority == Priority.LOW_PRIORITY) {
            return mLowPriorityQueue.add(aPriority);
        } else if (currentPriority == Priority.NORMAL_PRIORITY) {
            return mNormalPriorityQueue.add(aPriority);
        } else if (currentPriority == Priority.HIGH_PRIORITY) {
            return mHighPriorityQueue.add(aPriority);
        }
        return false;
    }

    /**
     * Get element from FairQueue with delete
     *
     * @return next element if success
     * null if otherwise
     */
    public Priority poll() {
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
                Priority result = mHighPriorityQueue.poll();
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
                Priority result = mNormalPriorityQueue.poll();
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
                Priority result = mLowPriorityQueue.poll();
                mRemainingItem--;
                lock.unlock();
                return result;
            }
        }

        return null;
    }

    /**
     * Check isEmpty
     *
     * @return true if FairQueue is empty, false if otherwise
     */
    public boolean isEmpty() {
        return mHighPriorityQueue.isEmpty() && mLowPriorityQueue.isEmpty() && mNormalPriorityQueue.isEmpty();
    }

    /**
     * return FairQueue size
     *
     * @return int - Item's count
     */
    public int size() {
        return mHighPriorityQueue.size() + mNormalPriorityQueue.size() + mLowPriorityQueue.size();
    }

    /** Go to the next Stage */
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

