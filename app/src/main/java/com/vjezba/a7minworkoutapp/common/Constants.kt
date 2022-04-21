package com.vjezba.a7minworkoutapp.common

import com.vjezba.a7minworkoutapp.ExerciseModel
import com.vjezba.a7minworkoutapp.R

object Constants {

    fun defaultExerciseList(): ArrayList<ExerciseModel>{
        val exerciseList = ArrayList<ExerciseModel>()

        val jumpingJacks = ExerciseModel(
            1,
            "Jumping Jacks",
            R.drawable.ic_jumping_jacks,
            false,
            false
            )
        exerciseList.add(jumpingJacks)

        val lunge = ExerciseModel(
            2,
            "Lunge",
            R.drawable.ic_lunge,
            false,
            false
        )
        exerciseList.add(lunge)

        val squat = ExerciseModel(
            3,
            "Squat",
            R.drawable.ic_squat,
            false,
            false
        )
        exerciseList.add(squat)

        val highKneeRunning = ExerciseModel(
            4,
            "High knee running ",
            R.drawable.ic_high_knees_running_in_place,
            false,
            false
        )
        exerciseList.add(highKneeRunning)

        val stepUp = ExerciseModel(
            5,
            "Step Up",
            R.drawable.ic_step_up_onto_chair,
            false,
            false
        )
        exerciseList.add(stepUp)

        val crunch = ExerciseModel(
            6,
            "Abdominal crunch",
            R.drawable.ic_abdominal_crunch,
            false,
            false
        )
        exerciseList.add(crunch)

        val plank = ExerciseModel(
            7,
            "Plank",
            R.drawable.ic_plank,
            false,
            false
        )
        exerciseList.add(plank)

        val sidePlank = ExerciseModel(
            8,
            "Side Plank",
            R.drawable.ic_side_plank,
            false,
            false
        )
        exerciseList.add(sidePlank)

        val pushUp = ExerciseModel(
            9,
            "Push Up",
            R.drawable.ic_push_up,
            false,
            false
        )
        exerciseList.add(pushUp)

        val pushUpAndRotate = ExerciseModel(
            10,
            "Push Up and Rotate",
            R.drawable.ic_push_up_and_rotation,
            false,
            false
        )
        exerciseList.add(pushUpAndRotate)

        val tricepsDip = ExerciseModel(
            11,
            "Triceps Dip",
            R.drawable.ic_triceps_dip_on_chair,
            false,
            false
        )
        exerciseList.add(tricepsDip)

        val wallSit = ExerciseModel(
            12,
            "Wall Sit",
            R.drawable.ic_wall_sit,
            false,
            false
        )
        exerciseList.add(wallSit)

        return exerciseList
    }
}